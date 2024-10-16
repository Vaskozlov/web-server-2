// @ts-ignore
import JXG from 'https://cdn.jsdelivr.net/npm/jsxgraph/distrib/jsxgraphcore.mjs';

export function createBoard(elementId: string): JXG.JSXGraph {
    return JXG.JSXGraph.initBoard(elementId, {
        boundingbox: [-8, 8, 8, -8],
        showCopyright: false,
        showNavigation: false,
        zoomX: 6,
        zoomY: 6,
        zoom: {
            wheel: false
        },
        axis: true,
        defaultAxes: {
            x: {
                name: 'x',
                withLabel: true,
                label: {
                    position: 'rt',
                    offset: [-15, 10]
                },
                ticks: {
                    insertTicks: false,
                    scaleSymbol: 'R',
                    minorHeight: 0,
                    ticksDistance: 0.5
                }
            },
            y: {
                name: 'y',
                withLabel: true,
                label: {
                    position: 'rt',
                    offset: [10, 0]
                },
                ticks: {
                    scaleSymbol: 'R',
                    minorHeight: 0,
                    insertTicks: false,
                    ticksDistance: 0.5
                }
            }
        }
    });
}

export function drawAreas(board: JXG.JSXGraph) {
    board.create(
        "polygon",
        [[0, 0], [1, 0], [0, 0.5]], {
            fillcolor: "lightblue",
            fillOpacity: 0.8,
            withLines: false,
            vertices: {
                visible: false
            }
        })

    board.create(
        "polygon",
        [[0, 0], [1, 0], [1, -0.5], [0, -0.5]], {
            fillcolor: "lightblue",
            fillOpacity: 0.8,
            withLines: false,
            vertices: {
                visible: false
            },
            radius: 0
        })

    board.create("sector",
        [[0, 0], [-0.5, 0], [0, -0.5]],
        {
            fillcolor: "lightblue",
            fillOpacity: 0.8,
            vertices: {
                visible: false
            },
            radiuspoint: {
                visible: false
            },
            anglePoint: {
                visible: false
            },
            strokeWidth: 0,
        })
}
