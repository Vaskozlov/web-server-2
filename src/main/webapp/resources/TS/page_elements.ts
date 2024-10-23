import {Plot} from "./plot.js"
import {PointCheckTableManager} from "./table/point_check_table_manager.js";
import {stringToFloat} from "./lib/string_to_float.js";
import {Result} from "./lib/result.js";

export const r_selector = document.getElementById("r_selector") as HTMLSelectElement;

export const main_plot: Plot = new Plot("box1");

export const clear_button = document.getElementById("clear_button") as HTMLButtonElement;

export const point_check_table_manager = PointCheckTableManager.fromTableId("area-check-results-table");

export const allXButtons = Array.from(document.querySelectorAll(`button.x-button`)) as HTMLButtonElement[]

export function getRSelectorValue(): Result<number, Error> {
    if (r_selector.value === "NaN") {
        return Result.success(NaN);
    }

    return stringToFloat(r_selector.value);
}