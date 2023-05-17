import { ElevatorState } from "../components/Models";


export const baseUrl = "http://localhost:8080"


export const fetchStatuses = async (): Promise<ElevatorState[]> => {
    const response = await fetch(`${baseUrl}/status`);;
    return response.json();
}


export const startSimulation = async (floors: number, elevators: number) => {
    await fetch(`${baseUrl}/start-simulation?floors=${floors}&elevators=${elevators}`);
}


export const stopSimulation = async () => {
    await fetch(`${baseUrl}/stop-simulation`);
}


export const pickupFromFloor = async (floor: number, direction: number) => {
    await fetch(`${baseUrl}/pickup?floor=${floor}&direction=${direction}`, {method: 'PUT'});
}


export const updateElevator = async (elevator: number, floor: number) => {
    await fetch(`${baseUrl}/update?elevator=${elevator}&floor=${floor}`, {method: 'PUT'});
}