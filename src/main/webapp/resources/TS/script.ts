import {testPoint} from "./point_tester.js";
import {formSubmitionHandler} from "./form_sender.js"
import {clear_button, getCheckboxCheckedValues, main_plot, main_table_manager} from "./page_elements.js";

document.getElementById("main-form").onsubmit = formSubmitionHandler;

window.onload = () => {
    main_plot.setOnClickFunction(drawPointsOnPlotClick);
    drawPointsFromTable();
}

clear_button.onclick = async (_: Event) => {
    try {
        await clearTable();
    } catch (e) {
        alert("Failed to clear the table");
        console.log(e.message);
        console.log(e.stack);
    }
}

function drawPointsFromTable() {
    const rows = main_table_manager.getRows(1);

    for (const [x, y, r, is_in_area] of rows) {
        main_plot.drawPoint(x, y, r, is_in_area ? "green" : "red");
    }
}

async function drawPointsOnPlotClick(x: number, y: number) {
    for (const r of getCheckboxCheckedValues()) {
        await testPoint(x * r, y * r, r);
    }
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

    main_table_manager.clearTable();
    main_plot.removeAllPoints();
}
