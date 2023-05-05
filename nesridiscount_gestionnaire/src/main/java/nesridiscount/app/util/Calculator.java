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
     * calcule le résultat d'une formule
     * @param formula
     * @return le résultat du calcul ou null
     */
    public static Double calculate(String formula){
        // recherche et gestion des éléments entre parenthèses
        Matcher matcher = Pattern.compile("\\(.*\\)").matcher(formula);

        // gestion des parenthèses contenu dans le formule
        while(matcher.find() ){
            String match = matcher.group();

            String matchWithtoutParenthese = match.substring(1,match.length() - 1);

            formula = formula.replaceAll(Pattern.quote(match),BigDecimal.valueOf(Calculator.calculate(matchWithtoutParenthese) ).toPlainString() );
        }

        formula = formula.replaceAll("\\s","");

        Set<Entry<Character,Operator> > entries = Operator.operators.entrySet();

        // gestion des opérateurs super prioritaire
        for(Map.Entry<Character,Operator> entry : entries){
            Operator operator = entry.getValue();

            if(operator.isSuperPrioritor) formula = Calculator.calculateValue(entry.getKey(),formula,operator.calculator);
        }
        
        // gestion des opérateurs prioritaires
        for(Map.Entry<Character,Operator> entry : entries){
            Operator operator = entry.getValue();

            if(operator.isPrioritor) formula = Calculator.calculateValue(entry.getKey(),formula,operator.calculator);
        }

        // gestion des opérateurs restant
        for(Map.Entry<Character,Operator> entry : entries){
            Operator operator = entry.getValue();

            if(!operator.isSuperPrioritor && !operator.isPrioritor) formula = Calculator.calculateValue(entry.getKey(),formula,operator.calculator);
        }

        try{
            return Double.parseDouble(formula);
        }
        catch(Exception e){
            return null;
        }
    }

    /**
     * calcul les valeurs liés à une formule
     * @param operatorSymbol
     * @param simpleFormula
     * @return la formule modifié
     */
    private static String calculateValue(Character operatorSymbol,String simpleFormula,OperatorCalc calculator){
        String symbol = Pattern.quote(String.valueOf(operatorSymbol) );

        Matcher matcher = Pattern.compile("([0-9\\.]+" + symbol + "[0-9\\.]+" + "(" + symbol +"[0-9\\.]+)*)").matcher(simpleFormula);

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

            simpleFormula = simpleFormula.replaceAll(Pattern.quote(match),result == null ? "0" : BigDecimal.valueOf(result).toPlainString() );
        }

        return simpleFormula;
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

        /**
         * enregistre un opérateur dans la liste des opérateurs géré
         * @param sign
         * @param calcultator
         * @param isPrioritor
         * @param isSuperPrioritor
         */
        public static void registerOperator(char sign,OperatorCalc calcultator,boolean isPrioritor,boolean isSuperPrioritor){
            Operator.operators.put(sign,new Operator(calcultator,isPrioritor,sign,isSuperPrioritor) );
        }
    }
}