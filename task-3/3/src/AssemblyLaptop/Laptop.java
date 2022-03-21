package AssemblyLaptop;

public class Laptop implements IProduct {
    private IProductPart firstPart;
    private IProductPart secondPart;
    private IProductPart thirdPart;


    @Override
    public void installFirstPart(IProductPart productPart) {
        this.firstPart = productPart;
    }

    @Override
    public void installSecondPart(IProductPart productPart) {
        this.secondPart = productPart;
    }

    @Override
    public void installThirdPart(IProductPart productPart) {
        this.thirdPart = productPart;
    }
}
