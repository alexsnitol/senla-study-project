public class Laptop implements IProduct {
    private IProductPart[] components;

    Laptop() {
        this.components = new IProductPart[3];
    }

    @Override
    public void installFirstPart(IProductPart productPart) {
        components[0] = productPart;
    }

    @Override
    public void installSecondPart(IProductPart productPart) {
        components[1] = productPart;
    }

    @Override
    public void installThirdPart(IProductPart productPart) {
        components[2] = productPart;
    }
}
