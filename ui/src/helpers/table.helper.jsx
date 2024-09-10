import moment from "moment";
import { roleToDisplay } from "./helpers"

const compareDateRow = (a, b, colId) => {
  const dateA = moment(a.original[colId])
  const dateB = moment(b.original[colId])
  return dateA > dateB ? 1 : dateA < dateB ? -1 : 0
}

export const columnTemplates = {
  hotels:
    [
      {
        header: "Id",
        accessorKey: "id",
        size: 64,
      },
      {
        header: "Name",
        accessorKey: "name",
        size: 15,
      },
      {
        header: "Title",
        accessorKey: "title",
        size: 20,
      },
      {
        header: "City",
        accessorKey: "city",
        size: 15,
      },
      {
        header: "Address",
        accessorKey: "address",
        size: 20,
      },
      {
        header: "Distance",
        accessorKey: "distance",
        size: 10,
      },
      {
        header: "Rating",
        accessorKey: "rating",
        size: 10,
      },
      {
        header: "Ratings count",
        accessorKey: "ratingsCount",
        size: 10,
      },
    ],
  rooms:
    [
      {
        header: "Id",
        accessorKey: "id",
        size: 64,
      },
      {
        header: "Name",
        accessorKey: "name",
        size: 20,
      },
      {
        header: "Description",
        accessorKey: "description",
        size: 50,
      },
      {
        header: "Number",
        accessorKey: "number",
        size: 10,
      },
      {
        header: "Price",
        accessorKey: "price",
        size: 25,
      },
      {
        header: "Guests number",
        accessorKey: "guestsNumber",
        size: 5,
      },
      {
        header: "Hotel Id",
        accessorKey: "hotelId",
        size: 10,
      },
    ],
  users:
    [
      {
        header: "Id",
        accessorKey: "id",
        size: 64,
      },
      {
        header: "Name",
        accessorKey: "name",
        size: 35,
      },
      {
        header: "Email",
        accessorKey: "email",
        size: 50,
      },
      {
        header: "Roles",
        accessorFn: row => row.roles.map(role => roleToDisplay(role)).join(", "),
        size: 25,
      }
    ],
  bookings: [
    {
      header: "Id",
      accessorKey: "id",
      size: 64,
    },
    {
      header: "Room Id",
      accessorKey: "roomId",
      size: 25,
    },
    {
      header: "User Id",
      accessorKey: "userId",
      size: 25,
    },
    {
      header: "Check in",
      accessorKey: "checkInDate",
      size: 25,
      sortingFn: (rowA, rowB, columnId) => compareDateRow(rowA, rowB, columnId),
    },
    {
      header: "Check out",
      accessorKey: "checkOutDate",
      size: 25,
      sortingFn: (rowA, rowB, columnId) => compareDateRow(rowA, rowB, columnId),
    },
  ],
  logins: [
    {
      header: "Timestamp",
      accessorKey: "timestamp",
      sortingFn: (rowA, rowB, columnId) => compareDateRow(rowA, rowB, columnId),
      size: 186,
    },
    {
      header: "User Id",
      accessorKey: "userId",
      size: 10,
    },
    {
      header: "Name",
      accessorKey: "name",
      size: 25,
    },
    {
      header: "Device",
      accessorKey: "device",
      size: 25,
    },
    {
      header: "OS",
      accessorKey: "os",
      size: 25,
    },
    {
      header: "Browser",
      accessorKey: "browser",
      size: 25,
    },
    {
      header: "Remote address",
      accessorKey: "remoteAddress",
      size: 25,
    },
  ],
  registrations: [
    {
      header: "Timestamp",
      accessorKey: "timestamp",
      sortingFn: (rowA, rowB, columnId) => compareDateRow(rowA, rowB, columnId),
      size: 186,
    },
    {
      header: "User Id",
      accessorKey: "userId",
      size: 33,
    },
    {
      header: "Name",
      accessorKey: "name",
      size: 33,
    },
    {
      header: "Email",
      accessorKey: "email",
      size: 33,
    },
  ],
  bookingStat: [
    {
      header: "Timestamp",
      accessorKey: "timestamp",
      sortingFn: (rowA, rowB, columnId) => compareDateRow(rowA, rowB, columnId),
      size: 186,
    },
    {
      header: "Room Id",
      accessorKey: "roomId",
      size: 25,
    },
    {
      header: "User Id",
      accessorKey: "userId",
      size: 25,
    },
    {
      header: "Check in",
      accessorKey: "checkInDate",
      size: 25,
      sortingFn: (rowA, rowB, columnId) => compareDateRow(rowA, rowB, columnId),
    },
    {
      header: "Check out",
      accessorKey: "checkOutDate",
      size: 25,
      sortingFn: (rowA, rowB, columnId) => compareDateRow(rowA, rowB, columnId),
    },
  ],
}
