import static org.junit.jupiter.api.Assertions.*;
class CalculationTest {

    @org.junit.jupiter.api.Test
    void calc() {
        Calculation sc=new Calculation("2.1+3.4");
        try{
            System.out.println(sc.Calc());

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}