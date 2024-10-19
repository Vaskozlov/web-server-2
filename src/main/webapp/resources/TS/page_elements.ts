import {Plot} from "./plot.js"
import {getAllCheckboxesByClass} from "./lib/query.js"
import {stringToFloat} from "./lib/string_to_float.js";
import {PointCheckTableManager} from "./table/point_check_table_manager.js";

const r_checkboxes = getAllCheckboxesByClass("r_value_checkbox");

export const main_plot: Plot = new Plot("box1");

export const clear_button = document.getElementById("clear_button") as HTMLButtonElement;

export const point_check_table_manager = PointCheckTableManager.fromTableId("area-check-results-table");

export function getRCheckboxesSelectedValues() {
    return r_checkboxes
        .filter(r_checkbox => r_checkbox.checked)
        .map(r_checkbox => stringToFloat(r_checkbox.value))
        .map(r_result => r_result.getValue());
}
