package storage;

public class Storage {
    private int capacity;
    private int numberOfProducts = 0;
    private Product[] products;

    public Storage(int capacity) {
        this.capacity = capacity;
        products = new Product[capacity];
    }

    public int getCapacity() {
        return this.capacity;
    }

    public Product[] getProducts() {
        return this.products;
    }

    public int addProduct(Product newProduct) {
        if (this.numberOfProducts == this.capacity)
            return -1;

        products[numberOfProducts] = newProduct;
        this.numberOfProducts++;
        return 0;
    }

    public void deleteProductById(int id) {
        if (id >= 0 && id < this.numberOfProducts) {
            if (id == this.numberOfProducts - 1) {
                this.products[id] = null;
            } else {
                for (int i = id; i < this.numberOfProducts - 1; i++) {
                    if (this.products[i + 1] != null) {
                        this.products[i] = this.products[i + 1];
                        this.products[i + 1] = null;
                    } else
                        break;
                }
            }
            this.numberOfProducts--;
        }
    }

    public Product getProductById(int id) {
        if (id >= 0 && id < this.numberOfProducts)
            return products[id];
        else
            return null;
    }

    public Product getProductByName(String name) {
        for (int i = 0; i < capacity; i++) {
            if (products[i] != null) {
                if (products[i].getName().equals(name)) {
                    return products[i];
                }
            } else {
                break;
            }
        }
        return null;
    }

    public int getNumberOfProducts() {
        return this.numberOfProducts;
    }

    public float sumOfWeight() {
        float sum = 0;
        for (int i = 0; i < capacity; i++) {
            if (products[i] != null)
                sum += products[i].getWeight();
            else
                break;
        }
        return sum;
    }

    public String getInfoOfProducts() {
        String result = "";
        for (int i = 0; i < capacity; i++) {
            if (products[i] != null) {
                result += products[i].toString();
                if (i != capacity - 1) {
                    if (products[i + 1] != null) {
                        result += "\n";
                    }
                }
            } else {
                break;
            }
        }
        return result;
    }

    public String getWeightOfProducts() {
        String result = "";
        for (int i = 0; i < capacity; i++) {
            if (products[i] != null) {
                result += products[i].getWeight();
                if (i != capacity - 1) {
                    if (products[i + 1] != null) {
                        result += "\n";
                    }
                }
            } else {
                break;
            }
        }
        return result;
    }
}