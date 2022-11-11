package com.nek;

public class Validacion {

    public static String toValidateNoIdentificacion(Integer codTipoDocumento, String noIdentificacion) {
        switch (codTipoDocumento) {
            case 1: // si es cedula
                if (noIdentificacion.length() != 10) { // si la cadena no tiene 10 caracteres
                    return "Una cédula se compone de 10 caracteres.";
                }
                if (toBuscarLetras(noIdentificacion)) { // si la cadena tiene espacios o letras
                    return "Una cédula no contiene letras.";
                }
                if (toValidarCedulaRuc(noIdentificacion)) { // si es un numero de documento válido
                    return "Cédula OK";
                } else {
                    return "Cédula No Válida";
                }
            case 2: // si es ruc
                if (noIdentificacion.length() != 13) {
                    return "El número de RUC se compone de 13 caracteres.";
                }
                if (toBuscarLetras(noIdentificacion)) {
                    return "Una número de RUC no contiene letras.";
                }
                if (toValidarCedulaRuc(noIdentificacion.substring(0, 10))) {
                    return "RUC OK";
                } else {
                    if (toValidarRucJuridica(noIdentificacion)) {
                        return "RUC OK";
                    }
                    return "La Cédula del RUC No es Válida";
                }

            default:
                return "Pasaporte OK";
        }
    }

    private static boolean toValidarRucJuridica(String noIdentificacion) {
        int[] numeros = new int[13];
        for (int i = 0; i < numeros.length; i++) {
            numeros[i] = Integer.parseInt(noIdentificacion.substring(i, (i + 1)));
        }
        int digitos2 = Integer.parseInt(String.valueOf(numeros[0]) + String.valueOf(numeros[1]));
        if (digitos2 > 24) { // EL PRIMEROS 2 DIGITOS ES POR LA PROVINCIA
            return false;
        }
        int digito3 = numeros[2];
        if (digito3 != 9) { // EL TERCER DIGITO DEBE SER 9
            return false;
        }
        // CALCULAR DIGITO VERIFICADOR CON MODULO 11
        int coeModulo11[] = { 4, 3, 2, 7, 6, 5, 4, 3, 2 }; // Coeficientes para RUC tipo Juridico
        int acumulado = 0;
        for (int i = 0; i < 9; i++) {
            acumulado += numeros[i] * coeModulo11[i];
        }
        int residuo = acumulado % 11;
        if (residuo == 0) { // SI EL RESIDUO ES 0, EL NUMERO VERFICADOR ES 0
            return numeros[9] == 0;
        } else {
            return numeros[9] == 11 - residuo; // CASO CONTRARIO ES 11 - RESIDUO
        }
    }

    private static boolean toBuscarLetras(String noIdentificacion) {
        for (int x = 0; x < noIdentificacion.length(); x++) {
            char c = noIdentificacion.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ') {
                return true; // encontro una letra o espacio
            }
        }
        return false; // no hay letras
    }

    private static boolean toValidarCedulaRuc(String noIdentificacion) {
        int ubicacion = Integer.parseInt(noIdentificacion.substring(0, 2));
        if ((ubicacion > 0 && ubicacion <= 24) || ubicacion == 30) { // del 1 al 24 las provincias del país, el 30 es
                                                                     // para extranjeros
            int[] numeros = new int[10];
            int[] productos = new int[numeros.length - 1];
            for (int i = 0; i < numeros.length; i++) {
                numeros[i] = Integer.parseInt(noIdentificacion.substring(i, (i + 1)));
            }
            // CICLO CON LOS PRIMEROS 9 NUMEROS
            for (int indice = 1; indice <= productos.length; indice++) {
                if ((indice % 2) == 0) {
                    // POSICIÓN PAR SE AGREGA EL NUMERO SIN CAMBIOS
                    productos[indice - 1] = numeros[indice - 1];
                } else {
                    // POSICIÓN IMPAR SE MULTIPLICA POR 2
                    int nproducto = numeros[indice - 1] * 2;
                    if (nproducto >= 10) { // SI EL PRODUCTO ES MAYOR O IGUAL A 10 LE RESTA 9
                        productos[indice - 1] = nproducto - 9;
                    } else { // SI ES MENOR A 10 AGREGA A LA LISTA
                        productos[indice - 1] = nproducto;
                    }
                }
            }
            int suma = 0; // SUMA TODOS LOS PRODUCTOS
            for (int producto : productos) {
                suma += producto;
            }
            if ((suma % 10) == 0) {
                return numeros[numeros.length - 1] == 0; // caso especial a modulo 10 si el numero es 0 entonces es
                                                         // valida
            } else {
                return (10 - (suma % 10)) == numeros[numeros.length - 1]; // si el residuo es igual al ultimo de la
                                                                          // cedula entonces es valida
            }
        }

        return false;
    }
}
