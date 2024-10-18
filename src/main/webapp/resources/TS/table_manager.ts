import {RowBuilder} from "./row_builder.js";
import {IllegalStateError} from "./illegal_state_error.js";

export class HtmlTableManager {
    private readonly table: HTMLTableElement;

    public static fromTableId(table_id: string): HtmlTableManager {
        const table = document.getElementById(table_id) as HTMLTableElement;
        return new HtmlTableManager(table);
    }

    public constructor(table: HTMLTableElement) {
        this.table = table;
    }

    public addRow(): RowBuilder {
        return new RowBuilder(this.table);
    }

    public removeRow(row_index: number): void {
        this.table.deleteRow(row_index);
    }

    public clearTable(): void {
        while (this.table.rows.length > 1) {
            this.table.deleteRow(1);
        }
    }

    public* getRows(start: number): Generator<[number, number, number, boolean]> {
        if (start < 0) {
            throw new IllegalStateError("Start index must be non-negative");
        }

        for (let i = start; i < this.table.rows.length; i++) {
            const row = this.table.rows[i];
            const cells = row.cells;

            const x = parseFloat(cells[0].innerText.trim());
            const y = parseFloat(cells[1].innerText.trim());
            const r = parseFloat(cells[2].innerText.trim());
            const is_in_area = cells[3].innerText.trim() === "yes";

            yield [x, y, r, is_in_area];
        }
    }
}
