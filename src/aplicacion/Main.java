package aplicacion;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import modelo.Empleado;
import modelo.EmpleadoFijo;
import modelo.EmpleadoPorHora;

/**
 * Main: controla la selección del mes, los menús, la captura de datos
 * y la impresión del reporte.
 */
public class Main {

    // ARREGLO SIMPLE: cantidad fija (12 meses), predefinido en el código.
    private static String[] meses = {
        "Enero", "Febrero", "Marzo", "Abril",
        "Mayo", "Junio", "Julio", "Agosto",
        "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    // ARRAYLIST: la cantidad crece conforme se ingresan empleados.
    // Guarda AMBOS tipos (polimorfismo) en una sola colección.
    private static ArrayList<Empleado> empleados = new ArrayList<Empleado>();

    private static Scanner scanner = new Scanner(System.in);

    // Atributo de clase: mes elegido (1-12). Se usa para consultar meses[numeroMes - 1].
    private static int numeroMes;

    public static void main(String[] args) {
        solicitarMes();
        menuPrincipal();
        System.out.println();
        System.out.println("Gracias por usar el sistema de nómina. ¡Hasta pronto!");
    }

    // ---------------------------------------------------------------
    // MES
    // ---------------------------------------------------------------
    private static void solicitarMes() {
        System.out.println("CÁLCULO DE NÓMINA");
        // Validar ANTES de acceder al arreglo
        numeroMes = leerEnteroEnRango("Ingrese el mes a trabajar (1-12): ", 1, 12);
    }

    // Título con el mes. Siempre se consulta meses[numeroMes - 1] (sin if/switch).
    private static String encabezado() {
        return "NÓMINA DEL MES: " + meses[numeroMes - 1].toUpperCase();
    }

    // ---------------------------------------------------------------
    // MENÚ PRINCIPAL
    // ---------------------------------------------------------------
    private static void menuPrincipal() {
        int opcion;
        do {
            limpiarPantalla();
            System.out.println(encabezado());
            System.out.println();
            System.out.println("1 - Ingreso de nómina");
            System.out.println("2 - Impresión de nómina");
            System.out.println("3 - Salir");
            System.out.println();
            opcion = leerEnteroEnRango("Seleccione una opción: ", 1, 3);

            if (opcion == 1) {
                ingresarNomina();
            } else if (opcion == 2) {
                imprimirNomina();
            }
        } while (opcion != 3);
    }

    // ---------------------------------------------------------------
    // OPCIÓN 1: INGRESO DE NÓMINA
    // ---------------------------------------------------------------
    private static void ingresarNomina() {
        boolean otro = true;
        while (otro) {
            limpiarPantalla();
            System.out.println(encabezado());
            System.out.println("INGRESO DE EMPLEADO A NÓMINA");
            System.out.println("Trabajadores actuales = " + empleados.size());
            System.out.println();
            System.out.println("1 - Empleado fijo");
            System.out.println("2 - Empleado por hora");
            System.out.println();
            int tipo = leerEnteroEnRango("Seleccione el tipo de empleado: ", 1, 2);

            // Aquí sí se permite if: es para elegir qué objeto crear.
            Empleado nuevo;
            if (tipo == 1) {
                nuevo = capturarEmpleadoFijo();
            } else {
                nuevo = capturarEmpleadoPorHora();
            }

            // Solo se agrega cuando TODOS los datos son válidos
            empleados.add(nuevo);

            System.out.println();
            System.out.println("Empleado agregado correctamente.");
            System.out.println("Trabajadores actuales = " + empleados.size());
            System.out.println();
            System.out.println("¿Desea ingresar otro empleado?");
            System.out.println("1 - Sí, ingresar otro empleado");
            System.out.println("2 - Volver al menú principal");
            System.out.println();
            int respuesta = leerEnteroEnRango("Seleccione una opción: ", 1, 2);
            otro = (respuesta == 1);
        }
    }

    private static Empleado capturarEmpleadoFijo() {
        limpiarPantalla();
        System.out.println(encabezado());
        System.out.println("INGRESO DE EMPLEADO FIJO");
        System.out.println();
        String dpi = leerDpi();
        String nombre = leerNombre();
        double sueldo = leerDecimalNoNegativo("Sueldo mensual (Q): ");
        double bonificacion = leerDecimalNoNegativo("Bonificación de este mes (Q): ");

        // Instanciación: new + constructor con argumentos
        EmpleadoFijo fijo = new EmpleadoFijo(dpi, nombre, sueldo, bonificacion);

        System.out.println();
        System.out.println("Pago del mes = sueldo mensual + bonificación");
        System.out.println("Pago de " + fijo.getNombre() + " = " + formatearQuetzales(fijo.calcularPago()));
        return fijo;
    }

    private static Empleado capturarEmpleadoPorHora() {
        limpiarPantalla();
        System.out.println(encabezado());
        System.out.println("INGRESO DE EMPLEADO POR HORA");
        System.out.println();
        String dpi = leerDpi();
        String nombre = leerNombre();
        double pagoHora = leerDecimalNoNegativo("Pago por hora (Q): ");
        double horas = leerDecimalNoNegativo("Horas trabajadas este mes: ");

        EmpleadoPorHora porHora = new EmpleadoPorHora(dpi, nombre, pagoHora, horas);

        System.out.println();
        System.out.println("Pago del mes = pago por hora x horas trabajadas");
        System.out.println("Pago de " + porHora.getNombre() + " = " + formatearQuetzales(porHora.calcularPago()));
        return porHora;
    }

    // ---------------------------------------------------------------
    // OPCIÓN 2: IMPRESIÓN DE NÓMINA (POLIMORFISMO)
    // ---------------------------------------------------------------
    private static void imprimirNomina() {
        limpiarPantalla();
        System.out.println(encabezado());
        System.out.println();

        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados en la nómina de este mes");
            System.out.println();
            System.out.println("Total de trabajadores: 0");
            System.out.println("Total de nómina: " + formatearQuetzales(0));
        } else {
            System.out.println(String.format(Locale.US, "%-16s%-14s%-10s%10s", "DPI", "Nombre", "Tipo", "Pago (Q)"));
            double total = 0;

            // Se recorre con referencias de tipo Empleado. Cada objeto ejecuta
            // SU PROPIA versión de calcularPago(); aquí no hay if/switch/instanceof.
            for (Empleado e : empleados) {
                double pago = e.calcularPago();
                total += pago;
                System.out.println(String.format(Locale.US, "%-16s%-14s%-10s%10.2f",
                        e.getDpi(), e.getNombre(), e.obtenerTipo(), pago));
            }

            System.out.println();
            System.out.println("Total de trabajadores: " + empleados.size());
            System.out.println("Total de nómina: " + formatearQuetzales(total));
        }

        System.out.println();
        System.out.print("Presione Enter para volver al menú principal...");
        scanner.nextLine();
    }

    // ---------------------------------------------------------------
    // MÉTODOS DE APOYO: lectura con validación (repiten hasta ser válido)
    // ---------------------------------------------------------------

    // Método con parámetros que DEVUELVE un resultado (int)
    private static int leerEnteroEnRango(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
                System.out.println("Error: ingrese un número entre " + minimo + " y " + maximo + ".");
            } catch (NumberFormatException ex) {
                System.out.println("Error: debe ingresar un número entero válido.");
            }
        }
    }

    private static String leerDpi() {
        while (true) {
            System.out.print("DPI: ");
            String dpi = scanner.nextLine().trim();
            if (dpi.matches("\\d{13}")) { // exactamente 13 dígitos
                return dpi;
            }
            System.out.println("Error: el DPI debe tener exactamente 13 dígitos numéricos.");
        }
    }

    private static String leerNombre() {
        while (true) {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) {
                return nombre;
            }
            System.out.println("Error: el nombre no puede estar vacío.");
        }
    }

    private static double leerDecimalNoNegativo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            try {
                double valor = Double.parseDouble(texto);
                if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                    System.out.println("Error: ingrese un valor numérico finito.");
                } else if (valor < 0) {
                    System.out.println("Error: el valor debe ser mayor o igual a 0.");
                } else {
                    return valor;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Error: debe ingresar un número válido (use punto para decimales).");
            }
        }
    }

    private static String formatearQuetzales(double monto) {
        return String.format(Locale.US, "Q %.2f", monto);
    }

    // Limpieza de pantalla: ANSI si hay consola real; si no, líneas en blanco.
    private static void limpiarPantalla() {
        if (System.console() != null) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } else {
            for (int i = 0; i < 40; i++) {
                System.out.println();
            }
        }
    }
}
