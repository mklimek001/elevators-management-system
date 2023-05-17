import React from 'react';
import { useState } from 'react';
import { startSimulation, stopSimulation } from '../api/APIutils';
import { Building } from './Building';


export const MainView = (props: any) => {
    const [floorsNumber, setFloorsNumber] = useState<number>(0);
    const [elevatorsNumber, setElevatorsNumber] = useState<number>(0);

    const [floorsNumberSaved, setFloorsNumberSaved] = useState<number>(0);
    const [elevatorsNumberSaved, setElevatorsNumberSaved] = useState<number>(0);

    const handleFloorsNumberChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.value != null) {
            setFloorsNumber(parseInt(e.target.value))
        }
    }

    const handleElevatorsNumberChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.value != null) {
            setElevatorsNumber(parseInt(e.target.value))
        }
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setFloorsNumberSaved(floorsNumber);
        setElevatorsNumberSaved(elevatorsNumber);
        await startSimulation(floorsNumber, elevatorsNumber);
    }


    const handleStop = async(e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        await stopSimulation();
    }

    return (
        <div style={{
            textAlign: "center",
            fontFamily: "system-ui",
            margin: "12px",
            width: '100%',
            height: '100%'
        }}>

            <form onSubmit={handleSubmit}>
            <label>
            Liczba pięter:
                <input
                name="floors"
                type="number"
                value={floorsNumber}
                min={0}
                max={100}
                onChange={e => handleFloorsNumberChange(e)}
                style={{margin: "10px"}} />
            </label>

            <br/>

            <label>
            Liczba wind:
                <input
                name="elevators"
                type="number"
                value={elevatorsNumber}
                min={0}
                max={16}
                onChange={e => handleElevatorsNumberChange(e)}
                style={{margin: "10px"}} />
            </label>

            <br/><br/>

            <label>
                <input
                    name="submit"
                    type="submit"
                    value="Rozpocznij"
                    style={{padding: "6px 12px"}}/>
            </label>
          
            </form>

            <button onClick={e => handleStop(e)}
                style={{padding: "6px 12px", margin: "10px"}}>
                Zatrzymaj
            </button>

            <Building floors={floorsNumberSaved} elevators={elevatorsNumberSaved}>
            </Building>    

        </div>
    )
}