package project.common;

public class PropertyValue {
    private double market_value;
    private double total_livable_area;
    private String zip_code;

    public PropertyValue(double market_value, double total_livable_area, String zip_code) {
        this.market_value = market_value;
        this.total_livable_area = total_livable_area;
        this.zip_code = zip_code;
    }

    /**
     * Get the market value for the property.
     */
    public double getMarketValue() {
        return market_value;
    }

    public double getTotalLivableArea() {
        return total_livable_area;
    }

    public String getZipCode() {
        return zip_code;
    }
}
