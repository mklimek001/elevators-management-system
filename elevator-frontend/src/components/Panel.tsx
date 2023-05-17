import React from 'react';
import { pickupFromFloor } from '../api/APIutils';


export const Panel = (props: any) => {
    const floor = props.floor

    const upRequest = async (e: React.MouseEvent<HTMLButtonElement>) => {
        pickupFromFloor(floor, 1);
    }

    const downRequest = async (e: React.MouseEvent<HTMLButtonElement>) => {
        pickupFromFloor(floor, -1);
    }

    return (
        <div style={{
            textAlign: "center",
            fontFamily: "system-ui",
            width: "100%",
            height: "100%",
            backgroundColor: "lightGray",
        }}>
            <div style={{paddingTop: "6px"}}>{floor}</div>
            <div>
                <button onClick={e => upRequest(e)}
                    style={{padding: "6px 22px", margin: "3px"}}>
                    ↑↑↑
                </button>

                <br/>

                <button onClick={e => downRequest(e)}
                    style={{padding: "6px 22px", margin: "3px"}}>
                    ↓↓↓
                </button>
            </div>
        </div>
    )
}