import {RowBuilder} from "./cell_builder.js";

export class HtmlTableRowManager {
    private readonly table: HTMLTableElement;

    public static fromTableId(table_id: string): HtmlTableRowManager {
        const table = document.getElementById(table_id) as HTMLTableElement;
        return new HtmlTableRowManager(table);
    }

    public constructor(table: HTMLTableElement) {
        this.table = table;
    }

    public addRow(): RowBuilder {
        return new RowBuilder(this.table);
    }

    public removeRow(row_index: number) {
        this.table.deleteRow(row_index);
    }

    public clearTable() {
        while (this.table.rows.length > 1) {
            this.table.deleteRow(1);
        }
    }
}
