import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathParserTest {

    @Test
    void GetStringNumber(){
        String expression="(15/(7-2*2))^2+10" ;
        MathParser mathpars = new MathParser(expression);
        assertEquals("15",mathpars.GetStringNumber( expression, 1));
    }

    @Test
    void ToPostfix(){
        String expression="(15/(7-2*2))^2+10" ;
        MathParser mathpars = new MathParser(expression);
        assertEquals("15 7 2 2 *-/2 ^10 +",mathpars.ToPostfix(expression));
    }

    @Test
    void Execute(){
        String expression="(15/(7-2*2))^2+10" ;
        MathParser mathpars = new MathParser(expression);
        assertEquals(25,mathpars.Execute('^',5.0,2.0));
    }

    @Test
    void Calc() {
        String expression="(15/(7-2*2))^2+10" ;
        MathParser mathpars = new MathParser(expression);
        assertEquals(35.0,mathpars.Calc());
    }
}