
function GridStatus({ data }) {
  return (
    <div className="card">
      <h2>⚡ Grid Status</h2>
      <h3>{data.status}</h3>
      <p>Voltage: {data.voltage}</p>
      <p>Frequency: {data.frequency}</p>
    </div>
  );
}

export default GridStatus;
