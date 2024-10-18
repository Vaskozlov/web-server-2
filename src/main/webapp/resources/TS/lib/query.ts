export function getAllCheckboxesByClass(class_name: string): HTMLInputElement[] {
    const checkboxes = document.querySelectorAll(`input[type="checkbox"].${class_name}`);
    return Array.from(checkboxes) as HTMLInputElement[];
}

export function createUrlWithParameters(
    endpoint: string,
    params: { [key: string]: any },
    base: string = window.location.href
): string {
    const url = new URL(endpoint, base);

    Object.keys(params)
        .filter(key => params.hasOwnProperty(key))
        .forEach(key => url.searchParams.append(key, params[key].toString()));

    return url.toString();
}
