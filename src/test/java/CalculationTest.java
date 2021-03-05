import static org.junit.jupiter.api.Assertions.*;

class CalculationTest {

    @org.junit.jupiter.api.Test
    void calcIntegers() {
        Calculation sc=new Calculation("5");
        try{
            assertTrue(5==sc.calc());
            sc.changeExpresion("1+6");
            assertTrue(7==sc.calc());
            sc.changeExpresion("12+6");
            assertTrue(18==sc.calc());
            sc.changeExpresion("51+63");
            assertTrue(114==sc.calc());
            sc.changeExpresion("15-6");
            assertTrue(9==sc.calc());
            sc.changeExpresion("6-20");
            assertTrue(-14==sc.calc());
            sc.changeExpresion("50/2");
            assertTrue(25==sc.calc());
            sc.changeExpresion("5*2");
            assertTrue(10==sc.calc());
            sc.changeExpresion("1+5^2");
            assertTrue(26==sc.calc());
        }catch (Exception ex){
            assertEquals("", ex.getMessage());
        }
    }
    @org.junit.jupiter.api.Test
    void calcDouble() {
        Calculation sc=new Calculation("5.1");
        try{
            assertTrue(5.1==sc.calc());
            sc.changeExpresion("0.1+6.2");
            assertEquals( 0.1+6.2, sc.calc());
            sc.changeExpresion("5.7-0.2");
            assertEquals( 5.7-0.2, sc.calc());
            sc.changeExpresion("5.2*9.8");
            assertEquals( 5.2*9.8, sc.calc(),0.01);
            sc.changeExpresion("20/3");
            assertEquals( 20.0/3, sc.calc(),0.01);

        }catch (Exception ex){
            assertEquals("", ex.getMessage());
        }
    }
    @org.junit.jupiter.api.Test
    void calcComplexExpressions() {
        Calculation sc=new Calculation("3+(2-1)");
        try{
            assertTrue(4==sc.calc());
            sc.changeExpresion("3/(2+6)");
            double temp=3.0/(2+6);
            assertEquals( temp, sc.calc(),0.0001);
            sc.changeExpresion("(3.0+5.0)/(5-4+5*(5+3+1-2))");
            temp=(3.0+5)/(5-4+5*(5+3+1-2));
            assertEquals(temp , sc.calc(),0.0001);
        }catch (Exception ex){
            assertEquals("", ex.getMessage());
        }
    }
    @org.junit.jupiter.api.Test
    void calcWithSpaces() {
        Calculation sc=new Calculation("3      +     4               ");
        try{
            assertTrue(7==sc.calc());
        }
        catch (Exception ex){
            assertEquals("", ex.getMessage());
        }
    }
    @org.junit.jupiter.api.Test
    void calcExeptions() {
        Calculation sc=new Calculation("5&2");
        try{
            sc.calc();
        }
        catch (Exception ex){
            assertEquals("Недопустимый знак", ex.getMessage());
        }
        sc.changeExpresion("5+-+2");
        try{
            sc.calc();
        }
        catch (Exception ex){
            assertEquals("Неправильно введенное выражение", ex.getMessage());
        }
        sc.changeExpresion("5+)5-8+2");
        try{
            sc.calc();
        }
        catch (Exception ex){
            assertEquals("Неправильно введенное выражение", ex.getMessage());
        }

    }
    @org.junit.jupiter.api.Test
    void calcWithVariables() {
        Calculation sc=new Calculation("a*b+c+b+k");
        try{
            ReadVariables read=new ReadVariables();
            assertEquals( 24, sc.calcWithVariables(read),0.01);

        }catch (Exception ex){
            assertEquals("", ex.getMessage());
        }

    }
}