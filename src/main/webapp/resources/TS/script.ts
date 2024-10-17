import {host_base_url} from "./config.js";
import {createBoard, drawAreas} from "./board.js";
import {stringToFloat} from "./string_to_float.js";
import {result_table_manager, testPointUsingRadius} from "./test_point.js";

const board = createBoard("box1");
const x_input_element = document.getElementById("x_input") as HTMLInputElement;
const y_input_element = document.getElementById("y_input") as HTMLInputElement;
const clear_table_button = document.getElementById("clear_table_button") as HTMLButtonElement;

window.onload = () => drawAreas(board);

document.getElementById("main-form").onsubmit = async function (event: Event) {
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

    await testPointUsingRadius(x, y, "r=1.0");
    await testPointUsingRadius(x, y, "r=1.5");
    await testPointUsingRadius(x, y, "r=2.0");
    await testPointUsingRadius(x, y, "r=2.5");
    await testPointUsingRadius(x, y, "r=3.0");
}

clear_table_button.onclick = function(event: Event) {
   try {
       doDelete().then();
   } catch (e) {
       alert("Failed to clear the table");
   }
}

async function doDelete() {
    const response = await fetch(`${host_base_url}/remove_points`, {
        method: "DELETE"
    });

    if (response.ok) {
        result_table_manager.clearTable();
    } else {
        throw new Error("Failed to clear the table");
    }
}
