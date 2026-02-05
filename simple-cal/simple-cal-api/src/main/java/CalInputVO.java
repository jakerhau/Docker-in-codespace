import com.fasterxml.jackson.annotation.JsonProperty;

public class CalInputVO {

    @JsonProperty("input1")
    private double input1;

    @JsonProperty("input2")
    private double input2;

    public CalInputVO() {
    }

    public CalInputVO(double input1, double input2, String method) {
        this.input1 = input1;
        this.input2 = input2;
        this.method = method;
    }

    @JsonProperty("method")
    private String method;

    public double getInput1() {
        return input1;
    }

    public void setInput1(double input1) {
        this.input1 = input1;
    }

    public double getInput2() {
        return input2;
    }

    public void setInput2(double input2) {
        this.input2 = input2;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
