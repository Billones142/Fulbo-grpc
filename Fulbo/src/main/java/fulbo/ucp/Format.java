package fulbo.ucp;

import java.text.DecimalFormat;

public class Format {
    static public String format(Double val) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###.00");
        return decimalFormat.format(val);
    }
}
