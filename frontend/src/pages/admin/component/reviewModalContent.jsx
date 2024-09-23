import { Button, TextField } from "@mui/material";
import * as React from "react";
import { useState } from "react";
import axios from "axios";

function ReviewModalContent(inputValue) {
  const { closer } = inputValue;
  const [value, setValue] = useState(inputValue);
  return (
    <div>
      <TextField
        label="ID"
        defaultValue={inputValue.id}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
        label="PORTFOLIOID"
        defaultValue={inputValue.portfolioId}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
        label="USERNAME"
        defaultValue={inputValue.username}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
        label="TITLE"
        defaultValue={inputValue.title}
        variant="standard"
        size="small"
        onChange={(event) => {
          const value = event.target.value;
          setValue((prev) => {
            return { ...prev, title: value };
          });
        }}
      />
      <TextField
        label="DESCRIPTION"
        defaultValue={inputValue.description}
        variant="standard"
        size="small"
        onChange={(event) => {
          const value = event.target.value;
          setValue((prev) => {
            return { ...prev, description: value };
          });
        }}
      />
      <TextField
        label="RATE"
        defaultValue={inputValue.rate}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
        label="CREATEDAT"
        defaultValue={inputValue.createdAt}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
        label="UPDATEDAT"
        defaultValue={inputValue.updatedAt}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <div className="btn-group">
        <Button
          onClick={() => {
            closer();
          }}
        >
          닫기
        </Button>
        <Button
          onClick={async (event) => {
            const response = await axios
              .patch(`/api/review/admin`, {
                id: value.id,
                title: value.title,
                description: value.description,
              })
              .catch((response) => console.log(response));
            console.log("role", value.role);
            console.log("value", value);
            console.log("event", event);
          }}
        >
          수정
        </Button>
      </div>
    </div>
  );
}

export default ReviewModalContent;
