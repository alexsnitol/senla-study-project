public class Storage {
    private int capacity;
    private int numberOfProducts = 0;
    private Product[] products;

    Storage(int capacity) {
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
        
        for (int i = 0; i < capacity; i++) {
            if (products[i] == null) {
                products[i] = newProduct;
                this.numberOfProducts++;
                return 0;
            }
        }
        return -2;
    }

    public void deleteProductById(int id) {
        products[id] = null;
        this.numberOfProducts--;
    }

    public Product getProducById(int id) {
        return products[id];
    }

    public Product getProductByName(String name) {
        for (int i = 0; i < capacity; i++) {
            if (products[i] != null) {
                if (products[i].getName() == name) {
                    return products[i];
                }
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
        }
        return sum;
    }

    public void printInfoOfProducts() {
        for (int i = 0; i < capacity; i++) {
            if (products[i] != null) {
                System.out.println(products[i].toString());
            }
        }
    }

    public void printWeightOfProducts() {
        for (int i = 0; i < capacity; i++) {
            if (products[i] != null) {
                System.out.println(products[i].getWeight());
            }
        }
    }
}