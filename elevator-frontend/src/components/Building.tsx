import React from 'react';
import { useState, useEffect } from 'react';
import { ElevatorState } from './Models';
import { fetchStatuses } from '../api/APIutils';
import { Elevator } from './Elevator';
import { Panel } from './Panel';


export const Building = (props: any) => {
    const [elevatorsStates, setElevatorsStates] = useState<ElevatorState[]>([]);
  
    const floors = props.floors;

    useEffect(() => {
      const loadData = async () => {
        const data = await fetchStatuses();
        setElevatorsStates(data);
      };

      loadData();

      const timer = setInterval(loadData, 200);
  
      return () => {
        clearInterval(timer);
      };
    }, []);


    const elevatorsAtFloor = (floor: number): JSX.Element[] =>{
        const floorsEl = [];
        floorsEl.push(<td key={-1} style={{border: '1px solid black'}}>
                          <Panel floor={floor}></Panel>
                      </td>) 
        let key = 0;
        for (const elevator of elevatorsStates){
            if(elevator.currentFloor === floor){
                floorsEl.push(<td key={key} style={{border: '1px solid black'}}>
                                  <Elevator id={elevator.elevatorId} floors={floors}></Elevator>
                              </td>)       
            }else{
                floorsEl.push(<td key={key} style={{border: '1px solid black'}}> </td>)
            }
            key++;
        }
        return floorsEl;
    }

    const renderBuilding = (): JSX.Element[] =>{
        const buildingFl = [];
        for (let i = floors-1; i >=0; i--) {
            buildingFl.push(<tr key={i} style={{height: '120px'}}>{elevatorsAtFloor(i)}</tr>)
        }
        return buildingFl;
    }


    return (
      <div style={{margin: '20px'}}>
        <table style={{ tableLayout: 'fixed', width: '100%', height: '100%' }}>
          <tbody>
            {renderBuilding()}
          </tbody>
        </table>
      </div>
    );
  };