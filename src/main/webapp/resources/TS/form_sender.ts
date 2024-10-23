import {testPoint} from "./point_tester.js";
import {stringToFloat} from "./lib/string_to_float.js";
import {getRSelectorValue} from "./page_elements.js";

const y_input_element = document.getElementById("y_input") as HTMLInputElement;

function setCustomValidity(input_element: HTMLInputElement, error: Error): void {
    input_element.setCustomValidity(error.message);
    input_element.reportValidity();
}

export async function formSubmitionHandler(event: Event, current_selected_x: string) {
    event.preventDefault();

    const x_result = stringToFloat(current_selected_x);
    const y_result = stringToFloat(y_input_element.value);
    const r_result = getRSelectorValue();

    if (x_result.isFailure()) {
        alert(`Bad x value: ${x_result.getError().message}`);
        return;
    }

    if (y_result.isFailure()) {
        setCustomValidity(y_input_element, y_result.getError());
        return;
    }

    if (r_result.isFailure()) {
        alert(`Bad r value: ${r_result.getError().message}`);
        return;
    }

    const x: number = x_result.getValue();
    const y: number = y_result.getValue();
    const r: number = r_result.getValue();

    await testPoint(x, y, r);
}