import {RowBuilder} from "./row_builder.js";

export class HtmlTableManager {
    protected readonly table: HTMLTableElement;

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
}
