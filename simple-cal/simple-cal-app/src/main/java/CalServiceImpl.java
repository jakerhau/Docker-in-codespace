import org.osgi.service.component.annotations.Component;

@Component(service = CalService.class)
public class CalServiceImpl implements CalService {

	@Override
	public CalResultVO cal(CalInputVO input) {
		CalResultVO result = new CalResultVO();

		try {
			double num;

			switch (input.getMethod()) {
				case "+":
					num = input.getInput1() + input.getInput2();
					break;
				case "-":
					num = input.getInput1() - input.getInput2();
					break;
				case "*":
					num = input.getInput1() * input.getInput2();
					break;
				case "/":
					if (input.getInput2() == 0) {
						return error("Division by zero");
					}
					num = input.getInput1() / input.getInput2();
					break;
				default:
					return error("Unknown method: " + input.getMethod());
			}

			result.setStatus("success");
			result.setResult(num);
			result.setError_message(null);
			return result;

		} catch (Exception e) {
			return error(e.getMessage());
		}
	}

	private CalResultVO error(String msg) {
		CalResultVO r = new CalResultVO();
		r.setStatus("error");
		r.setError_message(msg);
		r.setResult(0);
		return r;
	}
}
