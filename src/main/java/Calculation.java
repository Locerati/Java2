import java.text.Normalizer;
import java.util.Stack;
import java.util.*;

public class Calculation {
    private String expresion = new String();
    private Stack<Double> numbers =new Stack<Double>();
    private Stack<Character> signs=new Stack<Character>();
    private Map<Character, Integer> priority = new HashMap<Character, Integer>();
    public Calculation(String expr){
        expresion='('+expr+')';
        priority.put('(',-1);
        priority.put('*',1);
        priority.put('/',1);
        priority.put('+',2);
        priority.put('-',2);

    }
    public  double Calc() throws Exception{

        Wrapper<Integer> stance=new Wrapper<Integer>(0);
        Object curr;

        while (stance.get()<expresion.length()){
            curr =getCurrent(stance);

            if (curr instanceof Double) // Если операнд
            {
                numbers.push((double)curr);
            }
            else
                if (curr instanceof Character)
                {
                    if((char)curr!=')'){
                        while (mayPop((char) curr)){
                            popFunction();
                        }
                        signs.push((char)curr);
                    }
                    else
                    {
                        while (signs.size() > 0 && signs.peek() != '(')
                            popFunction();
                        signs.pop();

                    }

                }


        }
        if (numbers.size() > 1 || signs.size() > 0)
            throw new Exception("Неправильно введенное выражение");

        return  (double) Math.round(numbers.pop() * 10000) / 10000;
    }
    private void popFunction()
    {
        double secondNumber = numbers.pop();
        double firstNumber = numbers.pop();
        switch (signs.pop())
        {
            case '+':
                numbers.push(firstNumber+secondNumber);
                break;
            case '-':
                numbers.push(firstNumber-secondNumber);
                break;
            case '*':
                numbers.push(firstNumber*secondNumber);
                break;
            case '/':
                numbers.push(firstNumber/secondNumber);
                break;
        }
    }
    private boolean mayPop(char op) throws Exception{
        if(signs.size()==0){
            return false;
        }

            int p1 = getPriority(op);
            int p2 = getPriority(signs.peek());


        return   p1 >= p2 && p1 >= 0 && p2 >= 0;

    }
    private int getPriority (char op) throws Exception
    {
        for(Map.Entry<Character, Integer> item:priority.entrySet()){
            if (item.getKey()==op){
                return item.getValue();
            }

        }
        throw new Exception("Недопустимый знак");
    }
    private Object getCurrent(Wrapper<Integer> stance){
        if (expresion.charAt(stance.get())==' ' && stance.get()<expresion.length()){
            addOne(stance);

        }
        if (stance.get()==expresion.length()) {
          return null;
        }
        if (Character.isDigit(expresion.charAt(stance.get()))){
            String result="";
            while (stance.get()<expresion.length()&&(expresion.charAt(stance.get())=='.'||Character.isDigit(expresion.charAt(stance.get()))))
            {
                result+=expresion.charAt(stance.get());
                addOne(stance);
            }
            return Double.parseDouble(result);
        }
        else
        {
            addOne(stance);
            return expresion.charAt(stance.get()-1);
        }


    }
    private static void addOne( Wrapper<Integer> ref ) {
        int i = ref.get();
        ref.set( ++i  );
    }

}

