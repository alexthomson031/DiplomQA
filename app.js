const fs = require('fs');
const express = require('express');
const { v4: uuidv4 } = require('uuid');

let data;
try {
  data = JSON.parse(fs.readFileSync('data.json', 'utf-8'));
} catch (err) {
  console.error('Error reading data.json:', err.message);
  process.exit(1);
}

const server = express();
server.use(express.json());

server.post(['/payment', '/credit'], (req, res) => {
  console.log(`Incoming request: ${req.path} ${JSON.stringify(req.body)}`);
  const { body: { number } } = req;

  if (!number) {
    res.status(400).send({ error: 'Missing "number" in request body' });
    return;
  }

  const [item] = data.filter(o => o.number === number);
  if (item === undefined) {
    res.status(400).send({ error: 'Item not found' });
    return;
  }

  res.send({
    id: uuidv4(),
    status: item.status,
  });
});

const port = process.env.PORT || 9999;
server.listen(port, () => {
  console.log(`Server running on port ${port}`);
});
