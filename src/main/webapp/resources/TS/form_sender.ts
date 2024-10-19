import {testPoint} from "./point_tester.js";
import {stringToFloat} from "./lib/string_to_float.js";
import {getRCheckboxesSelectedValues} from "./page_elements.js";

const x_input_element = document.getElementById("x_input") as HTMLInputElement;
const y_input_element = document.getElementById("y_input") as HTMLInputElement;

function setCustomValidity(input_element: HTMLInputElement, error: Error): void {
    input_element.setCustomValidity(error.message);
    input_element.reportValidity();
}

export async function formSubmitionHandler(event: Event) {
    event.preventDefault();

    const x_result = stringToFloat(x_input_element.value);
    const y_result = stringToFloat(y_input_element.value);

    if (x_result.isFailure()) {
        setCustomValidity(x_input_element, x_result.getError());
        return;
    }

    if (y_result.isFailure()) {
        setCustomValidity(y_input_element, y_result.getError());
        return;
    }

    const x: number = x_result.getValue();
    const y: number = y_result.getValue();

    for (const r of getRCheckboxesSelectedValues()) {
        await testPoint(x, y, r);
    }
}