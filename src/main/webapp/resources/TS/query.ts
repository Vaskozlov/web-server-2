export function getAllCheckboxesByClass(className: string): HTMLInputElement[] {
    const checkboxes = document.querySelectorAll(`input[type="checkbox"].${className}`);
    return Array.from(checkboxes) as HTMLInputElement[];
}
