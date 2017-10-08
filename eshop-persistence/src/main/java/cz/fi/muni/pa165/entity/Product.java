package cz.fi.muni.pa165.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name="ESHOP_PRODUCTS")
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable=false,unique=true)
    private String name;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Temporal(TemporalType.DATE)
    private Date dateAdded;

    public Product(Long productId) {
        this.id = productId;
    }

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getAddedDate() { return dateAdded; }

    public void setAddedDate(Date dateAdded) { this.dateAdded = dateAdded; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setColor(Color color) { this.color = color; }

    public Color getColor() { return color; }

    @Override
    public int hashCode() {
        final int prime = 37;
        int result = 1;
        result = prime * (result + ((getName() == null) ? 0 : getName().hashCode())
                                 + ((getAddedDate() == null) ? 0 : getAddedDate().hashCode())
                                 + ((getColor() == null) ? 0 : getColor().hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (! (obj instanceof Product))
            return false;
        Product other = (Product) obj;
        if (name == null) {
            if (other.getName() != null)
                return false;
        } else if (!name.equals(other.getName()))
            return false;
        return true;
    }
}
