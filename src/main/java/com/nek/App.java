package com.nek;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "VALIDACION DE RUC O CÉDULA ECUATORIANA:" );
        String identificacion = "0953114774";
       String res =  Validacion.toValidateNoIdentificacion(1, identificacion);
    
        System.out.println(identificacion.concat(" ").concat(res));
    }
}
