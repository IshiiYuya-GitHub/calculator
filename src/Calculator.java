import java.util.*;

public class Calculator {
    public static void main(String[] args) {
        // スキャナで式を取得
        Scanner scanner = new Scanner(System.in);
        System.out.print("式：");
        String inputFormula = scanner.nextLine();

        // "end"の入力で終了
        if (inputFormula.equals("end")) {
            System.out.println("終了します。");
            System.exit(0);
        }

            // スペースの類を空文字に変換
            inputFormula = inputFormula.replaceAll("\\s", "");
            inputFormula = inputFormula.replace("　", "");


            // String型のリストで式を個別に分けて収納
            ArrayList<String> formula = new ArrayList<>(Arrays.asList(inputFormula.split("(?<=[+\\-*/])|(?=[+\\-*/])")));

            formula = minusCheck(formula);

            int intResult = 0;
            double doubleResult = 0;
            double escape = 0;

            // 優先的な計算
            if (formula.contains("*") || formula.contains("/")) {
                for (int i = 0; i < formula.size();) {
                    if (formula.get(i).equals("*")) {
                        escape = convertDouble(formula.get(i-1)) * convertDouble(formula.get(i+1));
                        for (int j = 0; j < 3; j++) {
                            formula.remove(i-1);
                        }
                        formula.add(i-1, convertString(escape));
                        i -= 2;
                    } else if (formula.get(i).equals("/")) {
                        escape = convertDouble(formula.get(i-1)) / convertDouble(formula.get(i+1));
                        // 0除算を確認
                        if (convertDouble(formula.get(i+1)) == 0) {
                            System.out.println("0除算エラー");
                            main(args);
                        }
                        for (int j = 0; j < 3; j++) {
                            formula.remove(i-1);
                        }
                        formula.add(i-1, convertString(escape));
                        i -= 2;
                    }
                    i++;
                }
                doubleResult = escape;
            }

            // +-の実装
            for (int i = 0; i < formula.size(); i++) {
                if (i == 0) {
                    doubleResult = convertDouble(formula.get(0));
                }
                if (formula.get(i).equals("+")) {
                    doubleResult += convertDouble(formula.get(i + 1));
                } else if (formula.get(i).equals("-")) {
                    doubleResult -= convertDouble(formula.get(i + 1));
                }
            }

            if (checkInt(doubleResult)) {
                intResult = convertInt(doubleResult);
                System.out.println(intResult);
            } else  {
                System.out.println(doubleResult);
            }
            main(args);

    }


    // 少数を文字に変換
    private static String convertString(double dblNumber) {
        return String.valueOf(dblNumber);
    }

    // 文字を少数に変換
    private static double convertDouble(String strNumber) {
        try {
            return Double.parseDouble(strNumber);
        } catch (Exception e) {
            System.out.println("少数に変換できませんでした。");
            System.exit(-1);
        }
        return 0;
    }

    //　少数を整数に変換
    private static int convertInt(double dblNumber) {
        return (int) dblNumber;
    }

    // マイナスのチェック
    private static ArrayList<String> minusCheck(ArrayList<String> formula) {
        for (int i = 0; i < formula.size();i++) {
            if (i == 0) {
                if (formula.get(0).equals("-")) {
                    String temp = "-" + formula.get(1);
                    formula.subList(0, 2).clear();
                    formula.add(0, temp);

                }
            } else if (formula.get(i-1).matches("[+\\-*/]") && formula.get(i).equals("-") && formula.get(i+1).matches("[0-9]+")) {
                String temp = "-" + formula.get(i+1);
                for (int j = 0; j < 2; j++) {
                    formula.remove(i);
                }
                formula.add(i, temp);
            }
        }
        return formula;
    }

    // 整数に変換してもよいかのチェック
    private static boolean checkInt(double dblNumber) {
        String check = convertString(dblNumber);
        return check.matches(".*0$");
    }
}
