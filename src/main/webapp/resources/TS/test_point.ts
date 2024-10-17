import {stringToFloat} from "./string_to_float.js";
import {host_base_url} from "./config.js";
import {HtmlTableRowManager} from "./table_manager.js";

const dom_parser = new DOMParser();
export const result_table_manager = HtmlTableRowManager.fromTableId("area-check-results-table");

export async function testPointUsingRadius(x: number, y: number, r_selector_name: string) {
    const selector = document.getElementById(r_selector_name) as HTMLInputElement;

    if (selector.checked) {
        const r_value = stringToFloat(selector.name);

        if (r_value.isFailure()) {
            alert(`Incorrect radius: ${r_value.getError()}`);
            return;
        }

        await testPoint(x, y, r_value.getValue());
    }
}

async function testPoint(x: number, y: number, r: number) {
    const url = new URL(`${host_base_url}/check_point`);
    url.searchParams.append('x', x.toString());
    url.searchParams.append('y', y.toString());
    url.searchParams.append('r', r.toString());

    console.log(url.toString());

    try {
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
    } catch (e) {
        alert(`Error: ${e}`);
    }
}

async function handleSucceedResponse(response: Response) {
    const response_text: string = await response.text();
    const resulted_document: Document = dom_parser.parseFromString(response_text, "text/html");
    const table = resulted_document.getElementById("area-check-results-table") as HTMLTableElement;
    const second_row = table.rows[1];
    const row_builder = result_table_manager.addRow();

    for (let i = 0; i != second_row.cells.length; ++i) {
        row_builder.addCell(second_row.cells[i])
    }
}

async function handleBadResponse(response: Response) {
    if (response.status !== 200) {
        document.open();
        document.write(await response.text());
        document.close();
    }

    return;
}