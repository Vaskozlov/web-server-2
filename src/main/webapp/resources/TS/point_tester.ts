import {createUrlWithParameters} from "./lib/query.js";
import {main_plot, point_check_table_manager} from "./page_elements.js";

const dom_parser = new DOMParser();
const status_code_in_case_point_is_in_area = 200;

export async function testPoint(x: number, y: number, r: number) {
    const url = createUrlWithParameters('./check', {x, y, r});

    const response = await fetch(
        url,
        {
            method: 'GET'
        }
    );

    if (!response.ok) {
        handleBadResponse(url);
        return;
    }

    await addNewRowToTable(response);
    drawNewPoint(x, y, r, response.status === status_code_in_case_point_is_in_area);
}

function drawNewPoint(x: number, y: number, r: number, is_in_area: boolean) {
    const point_color = is_in_area ? "green" : "red";
    main_plot.drawPoint(x, y, r, point_color);
}

async function addNewRowToTable(response: Response) {
    const response_text: string = await response.text();
    const doc: Document = dom_parser.parseFromString(response_text, "text/html");

    const table = doc.getElementById("area-check-results-table") as HTMLTableElement;
    const second_row = table.rows[1];

    point_check_table_manager
        .addRow()
        .addCells(second_row.cells);
}

function handleBadResponse(url: string) {
    window.location.href = url;
}
