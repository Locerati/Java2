import java.util.Scanner;
public class ReadVariables implements IReadVariables {
    @Override
    public Double read(String message) {


        switch (message.charAt(message.length()-2)) {
        case 'a': return 7.0;
            case 'b': return 2.0;

            case 'c': return 3.0;
            case 'd': return 4.0;
                default:
                    return 5.0;
            }

    }
}
