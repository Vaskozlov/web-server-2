import {stringToFloat} from "./string_to_float.js";
import {main_plot, server_base_url} from "./globals.js";
import {HtmlTableManager} from "./table_manager.js";

const dom_parser = new DOMParser();
export const main_table_manager = HtmlTableManager.fromTableId("area-check-results-table");

export async function testPointUsingRadiusSelector(
    x: number,
    y: number,
    r_checkbox: HTMLInputElement,
    are_coordinates_normalized: boolean = false
) {
    if (r_checkbox.checked) {
        const r_value = stringToFloat(r_checkbox.name);

        if (r_value.isFailure()) {
            alert(`Incorrect radius: ${r_value.getError()}`);
            return;
        }

        const r = r_value.getValue();

        x *= are_coordinates_normalized ? r : 1;
        y *= are_coordinates_normalized ? r : 1;

        await testPoint(x, y, r);
    }
}

async function testPoint(x: number, y: number, r: number) {
    const url = new URL(`${server_base_url}/check_point`);
    url.searchParams.append('x', x.toString());
    url.searchParams.append('y', y.toString());
    url.searchParams.append('r', r.toString());

    const response = await fetch(
        url.toString(),
        {
            method: 'GET'
        }
    );

    if (!response.ok) {
        await handleBadResponse(response);
        return;
    }

    await handleSucceedResponse(response);

    const is_in_area = response.status === 200;
    main_plot.drawPoint(x, y, r, is_in_area ? "green" : "red");
}

async function handleSucceedResponse(response: Response) {
    const response_text: string = await response.text();
    const resulted_document: Document = dom_parser.parseFromString(response_text, "text/html");
    const table = resulted_document.getElementById("area-check-results-table") as HTMLTableElement;
    const second_row = table.rows[1];
    const row_builder = main_table_manager.addRow();

    for (const cell of second_row.cells) {
        row_builder.addCell(cell)
    }
}

async function handleBadResponse(response: Response) {
    document.open();
    document.write(await response.text());
    document.close();
}
