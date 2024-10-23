import {formSubmitionHandler} from "./form_sender.js"
import {testPoint} from "./point_tester.js";
import {
    allXButtons,
    clear_button,
    getRSelectorValue,
    main_plot,
    point_check_table_manager,
    r_selector
} from "./page_elements.js";

let current_selected_x: string = "0";
const errorSpan = document.getElementById("r_selector_error") as HTMLSpanElement;

document.getElementById("main-form").onsubmit = async (ev) =>
    formSubmitionHandler(ev, current_selected_x);

window.onload = () => {
    main_plot.setOnClickFunction(drawPointsOnPlotClick);
    drawPointsFromTable();
    allXButtons.forEach(button => button.onclick = () => onXButtonClick(button));
    onXButtonClick(allXButtons[4]);
}

r_selector.onchange = () => {
    const rValue = getRSelectorValue().getValue();

    if (Number.isNaN(rValue)) {
        errorSpan.style.display = "inline";
    } else {
        errorSpan.style.display = "none";
        main_plot.redrawPoints(rValue);
    }
}

clear_button.onclick = clearTable;

function onXButtonClick(button: HTMLButtonElement) {
    current_selected_x = button.value;
    allXButtons.forEach(button => button.style.backgroundColor = "white");
    button.style.backgroundColor = "green";
}

function drawPointsFromTable() {
    const rows = point_check_table_manager.getRows(1);

    for (const [x, y, r, is_in_area] of rows) {
        main_plot.drawPoint(x, y, r, is_in_area ? "green" : "red");
    }
}

async function drawPointsOnPlotClick(x: number, y: number) {
    const r = getRSelectorValue().getValue();
    const errorSpan = document.getElementById("r_selector_error") as HTMLSpanElement;

    if (Number.isNaN(r)) {
        errorSpan.style.display = "inline";
        return;
    }

    errorSpan.style.display = "none";
    await testPoint(x * r, y * r, r);
}

async function clearTable() {
    const response = await fetch(
        `./remove_points`,
        {
            method: "DELETE"
        }
    );

    if (!response.ok) {
        throw new Error("Failed to clear the table");
    }

    point_check_table_manager.clearTable();
    main_plot.removeAllPoints();
}
