import java.text.Normalizer;
import java.util.Stack;
import java.util.*;

public class Calculation {
    private String expresion = new String();
    private Stack<Double> numbers =new Stack<Double>();
    private Stack<Character> signs=new Stack<Character>();
    private Map<Character, Integer> priority = new HashMap<Character, Integer>();
    private Map<Character, Double> variables = new HashMap<Character, Double>();
    private IReadVariables readVariables;
    public Calculation(String expr){
        expresion='('+expr+')';
        priority.put('(',-1);
        priority.put('^',1);
        priority.put('*',2);
        priority.put('/',2);
        priority.put('+',3);
        priority.put('-',3);


    }
    public void changeExpresion(String expr){
        expresion='('+expr+')';
    }
    public  double calcWithVariables(IReadVariables readVrbl) throws Exception{
        readVariables=readVrbl;
        double result=calc();
        readVariables=null;
        return result;
    }

    public  double calc() throws Exception{

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
                            if (numbers.size()>=2){
                            popFunction();
                            }
                            else {
                                throw new Exception("Неправильно введенное выражение");
                            }
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
            case '^':
                numbers.push(Math.pow(firstNumber,secondNumber));
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
    private Object getCurrent(Wrapper<Integer> stance) throws Exception{
        while (expresion.charAt(stance.get())==' ' && stance.get()<expresion.length()){
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
            if(readVariables!=null && Character.isLetter(expresion.charAt(stance.get()))){
                if (stance.get()<expresion.length()&&Character.isLetter(expresion.charAt(stance.get()+1)))
                    throw new Exception("Неподдерживаемые имена переменных");
                return variableToDouble(stance);

        }
        else
        {
            addOne(stance);
            return expresion.charAt(stance.get()-1);
        }


    }
    private double variableToDouble(Wrapper<Integer> stance){
        Character letter =expresion.charAt(stance.get());
        for(Map.Entry<Character,  Double> item:variables.entrySet()){
            if (item.getKey()==letter){
                double res= item.getValue();
                addOne(stance);
                return res;
            }
        }
        double value = readVariables.read("Enter variable - "+letter+':');
        addOne(stance);
        variables.put(letter,value);
        return value;
    }
    private static void addOne( Wrapper<Integer> ref ) {
        int i = ref.get();
        ref.set( ++i  );
    }

}

