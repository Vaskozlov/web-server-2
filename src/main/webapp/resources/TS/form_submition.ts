import {r_checkboxes} from "./globals.js";
import {stringToFloat} from "./string_to_float.js";
import {testPointUsingRadiusSelector} from "./test_point.js";

const x_input_element = document.getElementById("x_input") as HTMLInputElement;
const y_input_element = document.getElementById("y_input") as HTMLInputElement;

export async function formSubmitionHandler(event: Event) {
    event.preventDefault();

    const x_result = stringToFloat(x_input_element.value);
    const y_result = stringToFloat(y_input_element.value);

    if (x_result.isFailure()) {
        x_input_element.setCustomValidity(x_result.getError());
        x_input_element.reportValidity();
        return;
    }

    if (y_result.isFailure()) {
        y_input_element.setCustomValidity(y_result.getError());
        y_input_element.reportValidity();
        return;
    }

    const x: number = x_result.getValue();
    const y: number = y_result.getValue();

    for (const r_checkbox of r_checkboxes) {
        await testPointUsingRadiusSelector(x, y, r_checkbox, false);
    }
}