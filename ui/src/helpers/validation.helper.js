import { z } from "zod"

import { DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE } from "./search.helper"

const minMessage = (label, number) => `${label} must contain at least ${number} character(s)`
const maxMessage = (label, number) => `${label} must contain at most ${number} character(s)`
const positiveMessage = (label) => `${label} must be greater than 0`
const emailMessage = () => `Invalid email format`
const nonEmptyMessage = (label) => `${label} must contain at least one item`

export const validationSchemas = {
  loginSchema: z.object({
    name: z.string().trim().min(3, minMessage("Name", 3)).max(30, maxMessage("Name", 30)),
    password: z.string().trim().min(3, minMessage("Password", 3)).max(30, maxMessage("Password", 30)),
  }),

  registrationSchema: z.object({
    name: z.string().trim().min(3, minMessage("Name", 3)).max(30, maxMessage("Name", 30)),
    email: z.string().email(emailMessage()),
    password: z.string().trim().min(3, minMessage("Password", 3)).max(30, maxMessage("Password", 30)),
  }),

  hotelSchema: z.object({
    name: z.string().trim().min(3, minMessage("Name", 3)).max(30, maxMessage("Name", 30)),
    title: z.string().trim().min(3, minMessage("Title", 3)).max(300, maxMessage("Title", 300)),
    city: z.string().trim().min(3, minMessage("City", 3)).max(30, maxMessage("City", 30)),
    address: z.string().trim().min(3, minMessage("Address", 3)).max(100, maxMessage("Address", 100)),
    distance: z.number().positive(positiveMessage("Distance")),
  }),

  roomSchema: z.object({
    name: z.string().trim().min(3, minMessage("Name", 3)).max(30, maxMessage("Name", 30)),
    description: z.string().trim().min(3, minMessage("Description", 3)).max(300, maxMessage("Description", 300)),
    number: z.string().trim().min(3, minMessage("Name", 3)).max(30, maxMessage("Number", 30)),
    price: z.number().positive(positiveMessage("Price")),
    guestsNumber: z.number().positive(positiveMessage("Guests number")),
    hotelId: z.number().positive(positiveMessage("Hotel Id")),
  }),

  userSchema: z.object({
    name: z.string().trim().min(3, minMessage("Name", 3)).max(30, maxMessage("Name", 30)),
    email: z.string().email(emailMessage()),
    password: z.string().trim().min(3, minMessage("Password", 3)).max(30, maxMessage("Password", 30)),
    roles: z.array(z.string()).nonempty(nonEmptyMessage("Roles")),
  }),

  bookingSchema: z.object({
    userId: z.number().positive(),
    roomId: z.number().positive(),
    checkInDate: z.date().min(new Date()),
    checkOutDate: z.date(),
  }).refine(({ checkInDate, checkOutDate }) => checkOutDate > checkInDate, {
    message: "Check out date must be after check in date",
    path: ["checkOutDate"],
  }),

  paginationSchema: z.object({
    pageNumber: z.number().nonnegative().default(DEFAULT_PAGE_NUMBER),
    pageSize: z.number().positive().default(DEFAULT_PAGE_SIZE),
  }),

  filterSchema: z.object({
    pageNumber: z.number().nonnegative().default(DEFAULT_PAGE_NUMBER),
    pageSize: z.number().positive().default(DEFAULT_PAGE_SIZE),
    id: z.number().positive().optional().or(z.nan().optional()),
    hotelId: z.number().positive().optional().or(z.nan().optional()),
    name: z.string().optional(),
    title: z.string().optional(),
    city: z.string().optional(),
    address: z.string().optional(),
    distance: z.number().positive().optional().or(z.nan().optional()),
    rating: z.number().positive().optional().or(z.nan().optional()),
    ratingsCount: z.number().positive().optional().or(z.nan().optional()),
    minPrice: z.number().positive().optional().or(z.nan().optional()),
    maxPrice: z.number().positive().optional().or(z.nan().optional()),
    guestsNumber: z.number().positive().optional().or(z.nan().optional()),
    checkInDate: z.string().date().optional(),
    checkOutDate: z.string().date().optional(),
  }).superRefine((value, ctx) => {
    if (!!value.minPrice && !!value.maxPrice && value.maxPrice < value.minPrice) {
      ctx.addIssue({
        code: z.ZodIssueCode.invalid_arguments,
        message: "Max price must be greater or equal than min price",
        path: ["maxPrice"],
      })
    }
  
    if (value.checkOutDate <= value.checkInDate) {
      ctx.addIssue({
        code: z.ZodIssueCode.invalid_arguments,
        message: "Check out date must be after check in date",
        path: ["checkOutDate"],
      })
    }
  }),
}
