package modelo;

/**
 * CLASE ABSTRACTA: concentra los datos comunes (DPI y nombre).
 * No se puede hacer new Empleado(...). Las clases hijas definen el cálculo.
 */
public abstract class Empleado {

    // Atributos: pertenecen al objeto y viven mientras el objeto exista.
    private String dpi;
    private String nombre;

    // Constructor con parámetros. "this" distingue el atributo del parámetro.
    public Empleado(String dpi, String nombre) {
        this.dpi = dpi;
        this.nombre = nombre;
    }

    // Getters (no hay setters porque nadie necesita modificar estos datos)
    public String getDpi() {
        return dpi;
    }

    public String getNombre() {
        return nombre;
    }

    // Métodos abstractos: cada hija los resuelve con @Override
    public abstract double calcularPago();

    public abstract String obtenerTipo();
}
