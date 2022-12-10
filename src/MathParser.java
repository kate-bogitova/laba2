import java.util.*;

public class MathParser
{
    //	Хранит инфиксное выражение
    public String infixExpr;
    //	Хранит постфиксное выражение
    public String postfixExpr;

    //	Список и приоритет операторов


    public static final Map<Character, Integer> operationPriority;

    static {
        operationPriority = new HashMap<Character, Integer>();
        operationPriority.put('(',0);
        operationPriority.put('*', 2);
        operationPriority.put('/', 2);
        operationPriority.put('+', 1);
        operationPriority.put('-', 1);
        operationPriority.put( '^',3);
        operationPriority.put( '~',4);
    }

    //	Конструктор класса
    public MathParser(String expression)
    {
        //	Инициализируем поля
        infixExpr = expression;
        postfixExpr = ToPostfix(infixExpr + "\r");
    }

    public String GetStringNumber(String expr, Integer pos)
    {
        //	Хранит число
        String strNumber = "";

        //	Перебираем строку
        for (; pos < expr.length(); pos++)
        {
            //	Разбираемый символ строки
            char num = expr.charAt(pos);

            //	Проверяем, является символ числом
            if (Character.isDigit(num))
                //	Если да - прибавляем к строке
                strNumber += num;
            else
            {
                //	Если нет, то перемещаем счётчик к предыдущему символу
                pos--;
                //	И выходим из цикла
                break;
            }
        }

        //	Возвращаем число
        return strNumber;
    }

    private Integer change(String expr, Integer i)
    {
        int count = expr.length();
        i+=count;
        return i;
    }
    public String ToPostfix(String infixExpr)
    {
        //	Выходная строка, содержащая постфиксную запись
        String postfixExpr = "";
        //	Инициализация стека, содержащий операторы в виде символов
        Stack<Character> stack = new Stack<>();

        //	Перебираем строку
        for (int i = 0; i < infixExpr.length(); i++)
        {
            //	Текущий символ
            char c = infixExpr.charAt(i);

            //	Если симовол - цифра
            if (Character.isDigit(c))
            {
                //	Парсии его, передав строку и текущую позицию, и заносим в выходную строку
                postfixExpr += GetStringNumber(infixExpr, i) + " ";
                i=change(GetStringNumber(infixExpr, i),i)-1;
            }
            //	Если открывающаяся скобка
            else if (c == '(')
            {
                //	Заносим её в стек
                stack.push(c);
            }
            //	Если закрывающая скобка
            else if (c == ')')
            {
                //	Заносим в выходную строку из стека всё вплоть до открывающей скобки
                while (stack.size() > 0 && stack.peek() != '(')
                    postfixExpr += stack.pop();
                //	Удаляем открывающуюся скобку из стека
                stack.pop();
            }
            //	Проверяем, содержится ли символ в списке операторов
            else if (operationPriority.containsKey(c))
            {
                //	Если да, то сначала проверяем
                char op = c;
                //	Является ли оператор унарным символом
                if (op == '-' && (i == 0 || (i > 1 && operationPriority.containsKey( infixExpr.charAt(i-1) ))))
                    //	Если да - преобразуем его в тильду
                    op = '~';

                //	Заносим в выходную строку все операторы из стека, имеющие более высокий приоритет
                while (stack.size() > 0 && ( operationPriority.get(stack.peek()) >= operationPriority.get(op)))
                    postfixExpr += stack.pop();
                //	Заносим в стек оператор
                stack.push(op);
            }
        }
        //	Заносим все оставшиеся операторы из стека в выходную строку
        for (Character op : stack)
            postfixExpr += op;

        //	Возвращаем выражение в постфиксной записи
        return postfixExpr;
    }

    public Double Execute(Character op, Double first, Double second) {
        Double ans= Double.valueOf(0);
        switch(op)
        {
            case '+' :
                ans = first + second;               //	Сложение
                break;
            case '-':
                ans = first - second;					//	Вычитание
                break;
            case '*':
                ans = first * second;                //	Умножение
                break;
            case  '/':
                ans = first / second;          //	Деление
                break;
            case '^':
                ans = Math.pow(first, second);      //	Степень
                break;
        }
        return ans;
    }


    public double Calc()
    {
        //	Стек для хранения чисел
        Stack<Double> locals = new Stack<>();
        //	Счётчик действий
        int counter = 0;

        //	Проходим по строке
        for (int i = 0; i < postfixExpr.length(); i++)
        {
            //	Текущий символ
            char c = postfixExpr.charAt(i);

            //	Если символ число
            if (Character.isDigit(c))
            {
                //	Парсим
                String number = GetStringNumber(postfixExpr, i);
                i=change(GetStringNumber(postfixExpr, i),i);
                //	Заносим в стек, преобразовав из String в Double-тип
                locals.push(Double.valueOf(number));
            }
            //	Если символ есть в списке операторов
            else if (operationPriority.containsKey(c))
            {
                //	Прибавляем значение счётчику
                counter += 1;
                //	Проверяем, является ли данный оператор унарным
                if (c == '~')
                {
                    //	Проверяем, пуст ли стек: если да - задаём нулевое значение,
                    //	еси нет - выталкиваем из стека значение
                    double last = locals.size() > 0 ? locals.pop() : 0;

                    //	Получаем результат операции и заносим в стек
                    locals.push(Execute('-', Double.valueOf(0), last));
                    //	Отчитываемся пользователю о проделанной работе
                    System.out.println(counter +") " + c + " " + last + " = "  + locals.peek() );
                    //	Указываем, что нужно перейти к следующей итерации цикла
                    //	 для того, чтобы пропустить остальной код
                    continue;
                }

                //	Получаем значения из стека в обратном порядке
                double second = locals.size() > 0 ? locals.pop() : 0,
                        first = locals.size() > 0 ? locals.pop() : 0;

                //	Получаем результат операции и заносим в стек
                locals.push(Execute(c, first, second));
                //	Отчитываемся пользователю о проделанной работе
                System.out.println( counter + ")" + first + c + second + "=" + locals.peek());
            }
        }

        //	По завершению цикла возвращаем результат из стека
        return locals.pop();
    }
}

