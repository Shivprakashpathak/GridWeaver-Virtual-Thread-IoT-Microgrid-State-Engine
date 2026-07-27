import { useState, useEffect, useRef } from 'react';
import MapView from "./components/MapView";
import "./App.css";
import data from "./data";

import Sidebar from "./components/Sidebar";
import Header from "./components/Header";
import SolarCard from "./components/SolarCard";
import BatteryCard from "./components/BatteryCard";
import GridStatus from "./components/GridStatus";
import LoadCard from "./components/LoadCard";
import TemperatureCard from "./components/TemperatureCard";
function App() {
  const [nodes, setNodes]= useState([]);
  useEffect (()=>{
  
  const socket = new WebSocket('ws://localhost:8080');

  socket.onopen = () =>{
    console.log('WebSocket Connected!');
  };


  socket.onmessage = (event) =>{
    try {
      const incomingUpdate = JSON.parse(event.data);

      setNodes((prevNodes) =>
        prevNodes.map((node) =>
          node.id === incomingUpdate.id 
            ? {...node,...incomingUpdate } 
            : node 
        )
      );
    } catch (error) {
      console.error("Data parsing error:", error);
    }
  };
  return () => socket.close();
}, []);
 return (
     

    <div className="app">
      <Sidebar />

      <div className="main">
        <Header />

        <div className="cards">
          
          <SolarCard data ={data.solar}/>
          <BatteryCard data ={data.battery}/>
          <GridStatus data ={data.grid}/>
          <LoadCard data ={data.load}/>
          <TemperatureCard data ={data.temperature}/>
          <MapView/>
         

        </div>
      </div>
    </div>
  );
}

export default App;