import {
  Body,
  Controller,
  Delete,
  Get,
  Param,
  Post,
  Put,
  UseGuards,
} from '@nestjs/common';
import { NoteService } from './note.service';
import { NewNoteDto } from './dto/newNote.dto';
import { UpdateNoteDto } from './dto/updateNote.dto';
import { JwtAuthGuard } from '../auth/guards/jwtAuth.guard';
import { JwtPayload } from '../auth/interfaces/jwtPayload.interface';
import { User } from '../common/decorators/user.decorator';

@UseGuards(JwtAuthGuard)
@Controller('note')
export class NoteController {
  constructor(private noteService: NoteService) {}

  @Post('create')
  async create(@User() jwtUser: JwtPayload, @Body() newNoteDto: NewNoteDto) {
    return this.noteService.create(jwtUser.username, newNoteDto);
  }

  @Get('')
  async fetchAllForUser(@User() jwtUser: JwtPayload) {
    return this.noteService.fetchAllForUser(jwtUser.username);
  }

  @Delete(':id')
  async delete(@Param('id') id: string | number) {
    return this.noteService.delete(id);
  }

  @Put(':id')
  async update(@Body() dto: UpdateNoteDto, @Param('id') id: string | number) {
    return this.noteService.update(dto, id);
  }
}
