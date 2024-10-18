import {formSubmitionHandler} from "./form_submition.js"
import {main_plot, r_checkboxes, server_base_url} from "./globals.js";
import {main_table_manager, testPointUsingRadiusSelector} from "./test_point.js";

const clear_button = document.getElementById("clear_button") as HTMLButtonElement;

document.getElementById("main-form").onsubmit = formSubmitionHandler;

window.onload = () => {
    main_plot.setOnClickFunction(drawPointsOnPlotClick);
    drawPointsFromTable();
}

function drawPointsFromTable() {
    const rows = main_table_manager.getRows(1);

    for (const [x, y, r, is_in_area] of rows) {
        main_plot.drawPoint(x, y, r, is_in_area ? "green" : "red");
    }
}

async function drawPointsOnPlotClick(x: number, y: number) {
    for (const r_checkbox of r_checkboxes) {
        await testPointUsingRadiusSelector(x, y, r_checkbox, true);
    }
}

clear_button.onclick = async (_: Event) => {
    try {
        await clearTable();
    } catch (e) {
        alert("Failed to clear the table");
    }
}

async function clearTable() {
    const response = await fetch(
        `${server_base_url}/remove_points`,
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
