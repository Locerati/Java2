import java.text.Normalizer;
import java.util.Stack;
import java.util.*;
/**
 * Класс - позволяющий вычислять значения выражений
 * @author Denis Popov
 * @version 1.0
 */
public class Calculation {
    /** Выражение */
    private String expresion = new String();
    /** Выделинные операнды */
    private Stack<Double> numbers =new Stack<Double>();
    /** Взятые операторы */
    private Stack<Character> signs=new Stack<Character>();
    /** Хранение приоритетов знаков */
    private Map<Character, Integer> priority = new HashMap<Character, Integer>();
    /** Хранение значений переменных */
    private Map<Character, Double> variables = new HashMap<Character, Double>();
    private IReadVariables readVariables;
    public Calculation(String expr){
        expresion='('+expr+')';
        priority.put('(',-1);
        priority.put('^',1); /** Задаем приоритеты операций */
        priority.put('*',2);
        priority.put('/',2);
        priority.put('+',3);
        priority.put('-',3);


    }

    /**
     *  Метод вычисления значения выражений с переменными
     * @param readVrbl - принимает элемент, который представляет выражение
     * @return - возвращает результат найденого вычисления
     */
    public  double calcWithVariables(IReadVariables readVrbl) throws Exception{
        readVariables=readVrbl;
        double result=calc();
        readVariables=null;
        return result;
    }
    /**
     *  Метод вычисления значения выражений без переменных
     * @return - возвращает результат найденого выражения с округлением
     */
    public  double calc() throws Exception{

        Wrapper<Integer> stance=new Wrapper<Integer>(0);
        Object curr;

        while (stance.get()<expresion.length()){
            curr =getCurrent(stance);

            if (curr instanceof Double) // Если число то добавляем в стек
            {
                numbers.push((double)curr);
            }
            else
                if (curr instanceof Character)
                {
                    if((char)curr!=')'){
                        while (mayPop((char) curr)){ //Выталкиваем и добавляем новую
                            // операция в стек
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
                            popFunction(); // Удаляем открывающуюся скобу
                        signs.pop();

                    }

                }


        }
        // Проверка на правильность данных
        if (numbers.size() > 1 || signs.size() > 0)
            throw new Exception("Неправильно введенное выражение");

        return  (double) Math.round(numbers.pop() * 10000) / 10000;
    }
    /**
     *  Метод вычисление части выражения на определенном шаге
     */
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
    /**
     *  Метод проверяющий нужно ли производить выталкивание
     * @param op - принимает элемент, который представляет операцию
     */
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
        /**Делаем обход. Находим нужный нам знак и возврашаем приоритет
        соотвествующие данной операции*/
        for(Map.Entry<Character, Integer> item:priority.entrySet()){

            if (item.getKey()==op){
                return item.getValue();
            }

        }
        throw new Exception("Недопустимый знак");
    }
    /**
     *  Метод проверяющий нужно ли производить выталкивание
     * @param stance - счетчик текущей позиции, в перебираемом выражении
     */
    private Object getCurrent(Wrapper<Integer> stance) throws Exception{
        //Пропускаем лишние пробелы
        while (expresion.charAt(stance.get())==' ' && stance.get()<expresion.length()){
            addOne(stance);

        }
        //Если выражение закончилосьничего не делаем
        if (stance.get()==expresion.length()) {
          return null;
        }
        //Если операнд, начинаем составлять double
        if (Character.isDigit(expresion.charAt(stance.get()))){
            String result="";
            while (stance.get()<expresion.length()&&(expresion.charAt(stance.get())=='.'||Character.isDigit(expresion.charAt(stance.get()))))
            {
                result+=expresion.charAt(stance.get());
                addOne(stance);
            }
            return Double.parseDouble(result);
        }
        else //иначе проверяем является ли элемент  переменной
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
    /**
     *  Метод возвращает начение переменной
     * @param stance - счетчик текущей позиции, в перебираемом выражении
     */
    private double variableToDouble(Wrapper<Integer> stance){
        Character letter =expresion.charAt(stance.get());
        /*Делаем обход. Находим переменную и возврашаем её
         соотвествующее значение*/
        for(Map.Entry<Character,  Double> item:variables.entrySet()){
            if (item.getKey()==letter){
                double res= item.getValue();
                addOne(stance);
                return res;
            }
        }
        // Если переменая до этого не обявленна, то получаем ее.
        double value = readVariables.read("Enter variable - "+letter+':');
        addOne(stance);
        variables.put(letter,value);
        return value;
    }
    /**
     *  Метод увеличивает счетчик на 1
     * @param ref - счетчик текущей позиции, в перебираемом выражении
     */
    private static void addOne( Wrapper<Integer> ref ) {
        int i = ref.get();
        ref.set( ++i  );
    }

}

