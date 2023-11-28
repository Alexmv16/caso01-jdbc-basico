package app;

public class Articulo {

    private int id;
    private String nombre;
    private float precio;
    private String codigo;
    private int stock;
    private int grupo;
    private String descripcion;

    public Articulo(String nombre, float precio, String codigo, int grupo, int stock) {

        this.nombre = nombre;
        this.precio = precio;
        this.codigo = codigo;
        this.stock = stock;
        this.grupo = grupo;
    }

    public Articulo(int id, String nombre, float precio, String codigo, int grupo, int stock) {

        this.id=id;
        this.nombre = nombre;
        this.precio = precio;
        this.codigo = codigo;
        this.stock = stock;
        this.grupo = grupo;
    }
    public Articulo(int id, String nombre, float precio, String codigo,int grupo, int stock, String descripcion) {
        this.id=id;
        this.nombre = nombre;
        this.precio = precio;
        this.codigo = codigo;
        this.stock = stock;
        this.grupo = grupo;
        this.descripcion=descripcion;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Articulo) obj).getId() == this.getId();
    }

    @Override
    public String toString() {
        return id+" "+nombre+" "+precio+" "+stock+" "+grupo;
    }
}
