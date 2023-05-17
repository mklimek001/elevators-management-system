import React from 'react';
import { useState } from 'react';
import { updateElevator } from '../api/APIutils';


export const Elevator = (props: any) => {
    const [requestedFloor, setRequestedFloor] = useState<number>(0);
    const elevatorId = props.id

    const handleRequestsNumberChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.value != null) {
            setRequestedFloor(parseInt(e.target.value))
        }
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        await updateElevator(elevatorId, requestedFloor);
      }

    return (
        <div style={{
            textAlign: "center",
            fontFamily: "system-ui",
            margin: "3px",
            padding: "3px",
            width: "calc(100%-6px)",
            height: "calc(100%-6px)",
            backgroundColor: "lightBlue",
            borderRadius: "3px"
        }}>

            <form onSubmit={handleSubmit}>
            <label>
            Piętro:
            <br/>
                <input
                name="floor"
                type="number"
                value={requestedFloor}
                min={0}
                max={props.floors - 1}
                onChange={e => handleRequestsNumberChange(e)}
                style={{margin: "6px"}} />
            </label>

            <br/>

            <label>
                <input
                    name="submit"
                    type="submit"
                    value="OK"
                    style={{padding: "6px 12px", margin: "6px"}}/>
            </label>
          
            </form>    

        </div>
    )
}