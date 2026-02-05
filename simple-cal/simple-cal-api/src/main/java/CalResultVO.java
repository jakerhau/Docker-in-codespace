import com.fasterxml.jackson.annotation.JsonProperty;

public class CalResultVO {

    @JsonProperty("status")
    private String status;

    @JsonProperty("error_message")
    private String error_message;

    @JsonProperty("result")
    private double result;

    public CalResultVO(String status, String error_message, double result) {
        this.status = status;
        this.error_message = error_message;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CalResultVO() {
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    
}
