package nesridiscount.app.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * gestion du calcul d'une formule sous forme de chaine
 */
public abstract class Calculator {
    /**
     * calcule une formule sous forme de chaine
     * @param formula
     * @return le résultat
     */
    public static Double calculate(String formula){
        Set<Entry<Character,Operator> > entries = Operator.operators.entrySet();

        String operatorsRegex = "";
        String prioOperatorsRegex = "";

        // construction de la regex vérifiant la présence d'opérateur
        for(Map.Entry<Character,Operator> entry : entries ) {
            String quote = Pattern.quote(String.valueOf(entry.getKey() ) );

            operatorsRegex += quote + "|";

            Operator operator = entry.getValue();

            if(operator.isSuperPrioritor || operator.isPrioritor)
                prioOperatorsRegex += quote + "|";
        }

        int len = operatorsRegex.length();

        if(len > 0)
            operatorsRegex = operatorsRegex.substring(0,len - 1);
        
        len = prioOperatorsRegex.length();

        if(len > 0)
            prioOperatorsRegex = prioOperatorsRegex.substring(0,len - 1);

        double result = 0;

        formula = formula.replaceAll("\\s","");

        // tant qu'un opérateur est présdent
        while(Pattern.compile(operatorsRegex).matcher(formula).find() ){
            // gestion des opérateurs super prioritaire
            for(Map.Entry<Character,Operator> entry : entries){
                Operator operator = entry.getValue();

                if(operator.isSuperPrioritor) formula = Calculator.placeParenthesesAroundAndCalculate(entry.getKey(),formula,operator.calculator);
            }
            
            // gestion des opérateurs prioritaires
            for(Map.Entry<Character,Operator> entry : entries){
                Operator operator = entry.getValue();

                if(operator.isPrioritor) formula = Calculator.placeParenthesesAroundAndCalculate(entry.getKey(),formula,operator.calculator);
            }

            if(!Pattern.compile(prioOperatorsRegex).matcher(formula).find() ){
                // gestion des opérateurs restant
                for(Map.Entry<Character,Operator> entry : entries){
                    Operator operator = entry.getValue();

                    if(!operator.isSuperPrioritor && !operator.isPrioritor) formula = Calculator.placeParenthesesAroundAndCalculate(entry.getKey(),formula,operator.calculator);
                }
            }
        }

        return result;
    }

    /**
     * place des parenthès autour des occurences du symbole passé et fais le calcul
     * @param operatorSymbol
     * @param formula
     * @return la formule changée
     */
    private static String placeParenthesesAroundAndCalculate(char operatorSymbol,String formula,OperatorCalc calculator){
        String symbol = Pattern.quote(String.valueOf(operatorSymbol) );

        Matcher matcher = Pattern.compile("([0-9\\.]+" + symbol + "[0-9\\.]+" + "(" + symbol +"[0-9\\.]+)*)").matcher(formula);

        // remplacement des valeurs trouvés
        while(matcher.find() ){
            String match = matcher.group();

            Double result = null;

            // calcul du résultat du groupe
            for(String number : match.split(symbol) ){
                if(result == null){
                    result = Double.parseDouble(number);

                    continue;
                }

                try{
                    result = calculator.calculate(result,Double.parseDouble(number) );
                }
                catch(Exception e){}
            }

            formula = formula.replaceAll(Pattern.quote(match),result == null ? "0" : BigDecimal.valueOf(result).toPlainString() );
        }

        // suppression des parenthèses simple
        matcher = Pattern.compile("\\([0-9\\.]+\\)").matcher(formula);

        while(matcher.find() ){
            String match = matcher.group();

            formula = formula.replace(match,match.replace("(","").replace(")","") );
        }
            
        return formula;
    }

    /**
     * représente le calcul lié à un opérateur
     */
    public static interface OperatorCalc{
        /**
         * calcule le résultat de l'opérateur
         * @param arg1
         * @param arg2
         * @return le résultat du calcul
         */
        Double calculate(Double arg1,Double arg2) throws ArithmeticException;
    }

    /**
     * représente un opérareur mathématique
     */
    public static class Operator{
        private static HashMap<Character,Operator> operators = new HashMap<>(){{
            put('*',new Operator((arg1,arg2) -> arg1 * arg2,true,'*',true) );
            put('+',new Operator((arg1,arg2) -> arg1 + arg2,false,'+') );
            put('-',new Operator((arg1,arg2) -> arg1 - arg2,false,'-') );
            put('/',new Operator((arg1,arg2) -> arg1 / arg2,true,'/') );
            put('%',new Operator((arg1,arg2) -> arg1 % arg2,false,'%') );
        }};

        private boolean isPrioritor;
        private boolean isSuperPrioritor;

        private OperatorCalc calculator;

        private char linkedSymbol;

        public Operator(OperatorCalc calculator,boolean isPrioritor,char linkedSymbol){
            this.calculator = calculator;
            this.linkedSymbol = linkedSymbol;
            this.isPrioritor = isPrioritor;
            this.isSuperPrioritor = false;
        }

        public Operator(OperatorCalc calculator,boolean isPrioritor,char linkedSymbol,boolean isSuperPrioritor){
            this(calculator,isPrioritor,linkedSymbol);

            this.isSuperPrioritor = true;
        }

        /**
         * calcule le résultat de l'opérateur
         * @param arg1
         * @param arg2
         * @return le résultat du calcul
         */
        public Double getResult(Double arg1,Double arg2){
            return this.calculator.calculate(arg1,arg2);
        }

        public boolean getIsSuperPrioritor(){
            return this.isSuperPrioritor;
        }

        public boolean getIsPrioritor(){
            return this.isPrioritor;
        }

        public char getLinkedSymbol(){
            return this.linkedSymbol;
        }

        /**
         * enregistre un opérateur dans la liste des opérateurs géré
         * @param sign
         * @param calcultator
         * @param isPrioritor
         */
        public static void registerOperator(char sign,OperatorCalc calcultator,boolean isPrioritor){
            Operator.operators.put(sign,new Operator(calcultator,isPrioritor,sign) );
        }
    }
}