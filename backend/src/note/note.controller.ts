import {
  Body,
  Controller,
  Delete,
  Get,
  Param,
  Post,
  Put,
  UploadedFile,
  UseGuards,
  UseInterceptors,
} from '@nestjs/common';
import { NoteService } from './note.service';
import { NewNoteDto } from './dto/newNote.dto';
import { UpdateNoteDto } from './dto/updateNote.dto';
import { JwtAuthGuard } from '../auth/guards/jwtAuth.guard';
import { JwtPayload } from '../auth/interfaces/jwtPayload.interface';
import { User } from '../common/decorators/user.decorator';
import { FileInterceptor } from '@nestjs/platform-express';
import { Express } from 'express';
import { diskStorage } from 'multer';
import { v4 as uuidv4 } from 'uuid';
import { TagService } from '../tag/tag.service';

@UseGuards(JwtAuthGuard)
@Controller('note')
export class NoteController {
  constructor(
    private noteService: NoteService,
    private readonly tagService: TagService,
  ) {}

  @Post('create')
  create(@User() jwtUser: JwtPayload, @Body() newNoteDto: NewNoteDto) {
    return this.noteService.create(jwtUser.username, newNoteDto);
  }

  @Post('create/image')
  @UseInterceptors(
    FileInterceptor('file', {
      storage: diskStorage({
        destination: './static/imgs',
        filename: (req, file, callback) => {
          callback(undefined, uuidv4() + '.jpeg');
        },
      }),
    }),
  )
  createWithImage(
    @User() jwtUser: JwtPayload,
    @UploadedFile() file: Express.Multer.File,
    @Body() body: { text: string },
  ) {
    return this.noteService.create(jwtUser.username, {
      title: '',
      content: body.text,
      imageName: file.filename,
    });
  }

  @Post('create/audio')
  @UseInterceptors(
    FileInterceptor('file', {
      storage: diskStorage({
        destination: './static/audio',
        filename: (req, file, callback) => {
          callback(undefined, uuidv4() + '.mp3');
        },
      }),
    }),
  )
  createWithAudio(
    @User() jwtUser: JwtPayload,
    @UploadedFile() file: Express.Multer.File,
    @Body() body: { text: string },
  ) {
    return this.noteService.create(jwtUser.username, {
      title: '',
      content: body.text,
      audioName: file.filename,
    });
  }

  @Get('')
  fetchAllForUser(@User() jwtUser: JwtPayload) {
    return this.noteService.fetchAllForUser(jwtUser.username);
  }

  @Delete(':id')
  delete(@Param('id') id: string | number) {
    return this.noteService.delete(id);
  }

  @Put(':id')
  update(
    @User() jwtUser: JwtPayload,
    @Body() dto: UpdateNoteDto,
    @Param('id') id: string | number,
  ) {
    return this.noteService.update(jwtUser.username, dto, id);
  }

  @Post('tags/colors')
  updateTags(
    @User() jwtUser: JwtPayload,
    @Body() data: { data: Map<string, string> },
  ) {
    return this.tagService.updateColours(jwtUser.username, data.data);
  }
}
